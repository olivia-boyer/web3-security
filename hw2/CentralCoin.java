import java.util.*;

/**
 * CentralCoin - a centralized cryptocurrency using blockchain + merkle trees.
 *
 * Architecture:
 * - Blockchain: append-only log with hash pointers (Part 3)
 * - Each Block contains: merkle tree of transactions (Part 4)
 * - UTXOPool: derived state - rebuilt from blockchain (demonstrates concept)
 *
 * Workflow:
 * 1. Transactions accumulate in pending pool
 * 2. mineBlock() packages them into a block with merkle tree
 * 3. Block is appended to blockchain
 * 4. UTXO pool is updated to reflect new state
 */
public class CentralCoin {

    private final Wallet    theMint;
    private final Blockchain blockchain;
    private final UTXOPool  utxoPool;
    private final List<Transaction> pendingTxs;

    public CentralCoin()  {
        theMint = new Wallet(2048);    
        blockchain = new Blockchain();
        utxoPool = new UTXOPool();
        pendingTxs = new ArrayList<>();
    }

    public Transaction createCoins(double amount, Keys.PublicKey recipient) throws Exception {
        Transaction minting = new Transaction();
        minting.addOutput(amount, recipient);
        minting.computeHash();
        return minting;
    }

    
    public boolean processTransaction(Transaction tx) throws Exception {

        //verify transactions against utxo pool + pending transactions
        //plus if transactio itself makes sense math wise
    
        return false; 
    }

    
    public Block mineBlock()  {
        //verify block
        //add block to blockchain
        //rebuild utxo pool after mining by updating used inputs and new outputs
        return null;
        
    }

    public void rebuildUTXOPool() {
        //update utxo pool after block is mined
        
    }

    public Blockchain getBlockchain() { return blockchain; }
    public UTXOPool getUTXOPool() { return utxoPool; }
}
