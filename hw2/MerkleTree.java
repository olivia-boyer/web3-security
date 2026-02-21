import java.security.*;
import java.util.*;

public class MerkleTree {

    /** A node in the merkle tree (internal node or leaf). */
    private static class Node {
        byte[] hash;
        Node left, right;
        boolean isLeaf;

        Node(byte[] hash, boolean isLeaf) {
            this.hash = hash;
            this.isLeaf = isLeaf;
        }
    }

    private final Node root;
    private final List<byte[]> leafHashes;  // original transaction hashes

    
    public MerkleTree(List<byte[]> txHashes) {
         leafHashes = txHashes;

        List<MerkleTree.Node> leaves = new ArrayList<MerkleTree.Node>(leafHashes.size()); 
        for (int i = 0; i < leafHashes.size(); i++){
            Node toAdd = new Node(leafHashes.get(i), true);
            leaves.add(toAdd);
            if (leaves.size() % 2 != 0 && i == leafHashes.size()-1){
                leaves.add(toAdd);
            }
           // System.out.println(toAdd.hash);
        }
        this.root = buildTree(leaves, 0, leaves.size() - 1);
      
  
    }

    private Node buildTree(List<MerkleTree.Node> nodes, int start, int end) {
        //System.out.println("we goin " + start + " " + end);
       // System.out.println(start);
       // System.out.println(end);
        if (end - start == 1) {
            //System.out.println("adding node");
            Node left = nodes.get(start);
            //System.out.println(ns);
            Node right = nodes.get(end);
            Node retval = new Node(Utils.sha256(Utils.concat(left.hash, right.hash)), false);
            retval.left = left;
            retval.right = right;
            //System.out.println("making node...");
            return retval;
        } else if (start == end) {
      //  System.out.println("strategy failed");
        return null;
        }else {
            int mid = ((end - start) / 2) + start;
            //System.out.println(mid);
            Node left, right;
            if (((end - start) % 2) != 0) {
               // System.out.println("even");
             left = buildTree(nodes, start, mid);
             right = buildTree(nodes, mid+1, end);
            } else {
             left = buildTree(nodes, start, mid);
             right = buildTree(nodes, mid, end);
            }

            Node retval = new Node(Utils.sha256(Utils.concat(left.hash, right.hash)), false);
            retval.left = left;
            retval.right = right;
            return retval;
        }


    }

    public byte[] getRootHash() {
        return this.root.hash;
    }

    /**
     * Generates a proof that txHash is in the tree.
     * Returns the list of sibling hashes needed to reconstruct the root.
     * Returns null if txHash is not in the tree.
     */
    public List<byte[]> getProofOfInclusion(byte[] txHash) {
        int end = leafHashes.size();
        int start = 0;
        int idx = leafHashes.indexOf(txHash);
        if (idx == -1) {
          //  System.out.println("missing");
            return null;
        }
        List<byte[]> proof = new ArrayList<byte[]>();
        int mid = ((end +1)/ 2) + start;
        Node curNode = this.root;
        while (!curNode.isLeaf){
            if (idx < mid) {
                proof.add(curNode.right.hash);
                curNode = curNode.left;
                end = mid;
                mid = ((end +1)/ 2) + start;
            } else {
                proof.add(curNode.left.hash);
                curNode = curNode.right;
                start = mid + 1;
                mid = ((end +1)/ 2) + start;
            }
        }
        return proof;
    }

    /**
     * Verifies that txHash is in a tree with the given rootHash,
     * using the provided proof (list of sibling hashes).
     */
    public static boolean verifyProof(byte[] txHash, List<byte[]> proof, byte[] rootHash) {
   
        byte[] curHash = txHash;
        for (int i = proof.size() - 1; i >= 0; i--){
            curHash = Utils.sha256(Utils.concat(curHash, proof.get(i)));
        }
        if (Arrays.equals(curHash, rootHash)) {
            return true;
        }
        return false;


}
}