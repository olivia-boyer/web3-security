import java.util.List;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Block {
    private List<Transaction> transactions;
    private String timestamp;
    private HashPointer hp; //hash pointer to previous block
    private MerkleTree tree;

    public Block(List<Transaction> transactions, Block previousBlock) {
        this.transactions = transactions;
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
