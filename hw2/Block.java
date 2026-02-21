import java.util.List;
import java.util.ArrayList;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Block {
    private List<Transaction> transactions;
    private String timestamp;
    private HashPointer hp; //hash pointer to previous block
    private MerkleTree tree;

    public Block(List<Transaction> newtransactions, Block previousBlock) {
        if (newtransactions != null) {
        this.transactions = new ArrayList<Transaction>(newtransactions);
       // System.out.println(this.transactions);
        }
        if ((this.transactions != null) && (this.transactions.size() != 0)) {
        List<byte[]> txhashes = new ArrayList<byte[]>();
        for (int i = 0; i < transactions.size(); i++){
     
            txhashes.add(transactions.get(i).getHash());
        }
        this.tree = new MerkleTree(txhashes);
    }
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.hp = new HashPointer(this, previousBlock);
        //create merkle tree?
    }
    
    public List<Transaction> getTransactions() { 
        return this.transactions; 
    }
    
    public String getTimestamp() { 
        return this.timestamp; 
    }
    
    public Block getPrevious() { 
        return hp.dereference(); 
    }

    public HashPointer getHashPointer() {
        return this.hp;
    }

    public List<byte[]> getProofOfInclusion(Transaction tx) {
       return tree.getProofOfInclusion(tx.getHash());
    }

    public boolean verifyProof(byte[] txHash, List<byte[]> proof) {

            return MerkleTree.verifyProof(txHash, proof, tree.getRootHash());
   
        
    }
}
