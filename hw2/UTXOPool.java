import java.util.List;
import java.util.HashMap;

public class UTXOPool {

    public HashMap<Transaction.Input, Transaction.Output> pool;

    public UTXOPool () {
        this.pool = new HashMap<>();
        //list of outputs with associated inputs as keys??
    }


    public void addUTXO(byte[] txHash, int outIndex, Transaction.Output output) {
        Transaction.Input key = new Transaction.Input(txHash, outIndex);
       // System.out.println(txHash + " " + outIndex);
        pool.putIfAbsent(key, output);
        //txhash is input hash, and outIndex is self explanatory
        //can have same txHash but not same txHash and outPut
        return;
    }

    public void removeUTXO(byte[] txHash, int outIndex) {
        Transaction.Input key = new Transaction.Input(txHash, outIndex);
        pool.remove(key);
        return;
    }

    public Transaction.Output getOutput(byte[] txHash, int outIndex) {
        Transaction.Input key = new Transaction.Input(txHash, outIndex);

        //get output associated with those input details
        return pool.get(key);
    }

    public boolean contains(byte[] txHash, int outIndex) {
        Transaction.Input key = new Transaction.Input(txHash, outIndex);
        
        return pool.containsKey(key);
        //check if transaction is in pool
 
    }
}
