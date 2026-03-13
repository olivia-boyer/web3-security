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

    private final Wallet theMint;
    private final Blockchain blockchain;
    private final UTXOPool utxoPool;
    private final List<Transaction> pendingTxs;

    public CentralCoin() {
        theMint = new Wallet(2048);
        blockchain = new Blockchain();
        utxoPool = new UTXOPool();
        pendingTxs = new ArrayList<>();
    }

    public Transaction createCoins(double amount, Keys.PublicKey recipient) throws Exception {
        Transaction minting = new Transaction();
        minting.addOutput(amount, recipient);
        minting.computeHash();
        pendingTxs.add(minting);
        utxoPool.addUTXO(minting.getHash(), 0, minting.geOutputs().get(0));
        return minting;
    }

    public boolean processTransaction(Transaction tx) throws Exception {
        //check signatures
        //check amounts


        double totalInput = 0;
        for (int i = 0; i < tx.getInputs().size(); i++){
        Transaction.Output toCheck = utxoPool.getOutput(tx.getInputs().get(i).prevTxHash, tx.getInputs().get(i).prevOutIndex);
        if (toCheck == null) {
            return false;
        }
        totalInput += toCheck.value;
        if (!Wallet.verify(toCheck.recipient, tx.getInputDataToSign(i), tx.getInputs().get(i).signature)) {
            return false;
        }
    }
        double totalOutput = 0;
        for (int i = 0; i < tx.geOutputs().size(); i++) {
            totalOutput += tx.geOutputs().get(i).value;
        } 

        if (totalInput < totalOutput) {
            return false;
        }
       /* 
        List<Transaction.Input> inputs = tx.getInputs();
        int size = inputs.size();
        for (int i = 0; i < size; i++) {
          //  System.out.println(inputs.get(i).prevTxHash + " " + inputs.get(i).prevOutIndex);
            if (utxoPool.contains(inputs.get(i).prevTxHash, inputs.get(i).prevOutIndex)) {
            } else {
                return false;
            }
        }
        */

        // verify transactions against utxo pool + pending transactions
        // plus if transactio itself makes sense math wise

                pendingTxs.add(tx);
                return true;
    }

    public Block mineBlock() {
        // verify block

        // add block to blockchain
        blockchain.append(pendingTxs);
       // System.out.println(pendingTxs.get(0));
        pendingTxs.clear();
        // rebuild utxo pool after mining by updating used inputs and new outputs
        rebuildUTXOPool();
        return blockchain.getHead();

    }

    public void rebuildUTXOPool() {
        Block lastBlock = blockchain.getHead();
        List<Transaction> newtx = lastBlock.getTransactions();
       // System.out.println("size: " + newtx.size());
        for (int i = 0; i < newtx.size(); i++) {
            if (newtx.get(i).getInputs() != null) {
            for (int j = 0; j < newtx.get(i).getInputs().size(); j++) {
                Transaction.Input cur = newtx.get(i).getInputs().get(j);
                utxoPool.removeUTXO(cur.prevTxHash, cur.prevOutIndex);
            }
        }
        }

        for (int i = 0; i < newtx.size(); i++) {
            for (int j = 0; j < newtx.get(i).geOutputs().size(); j++) {
                utxoPool.addUTXO(newtx.get(i).getHash(), j, newtx.get(i).geOutputs().get(j));
            }
        }
        // update utxo pool after block is mined

    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public UTXOPool getUTXOPool() {
        return utxoPool;
    }
}
