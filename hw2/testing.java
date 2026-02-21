import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class testing {
   public static void main(String[] args) {
    int num = 1;
    List<byte[]> dets = new ArrayList<byte[]>(num);
    for (int i = 0; i < num; i++){
        byte[] toAdd = new byte[1];
        toAdd[0] = (byte) i;
        dets.add(toAdd);
    }

    MerkleTree tree = new MerkleTree(dets);
    System.out.println(tree.getRootHash()[0]);
    List<byte[]> proof = tree.getProofOfInclusion(dets.get(0));
    if (Arrays.equals(dets.get(0), proof.get(0))) {
        System.out.println("true");
    }
    
   } 
}
