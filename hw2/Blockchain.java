import java.util.*;

/**
 * Blockchain - An append-only, tamper-resistant log.
 *
 * A linked list of blocks connected by hash pointers.
 * Each block contains a list of transactions.
 * Any modification to a past block invalidates all subsequent hash pointers.
 */
public class Blockchain {

    private Block head;
    private int size;

    /**
     * Creates an empty blockchain.
     */
    public Blockchain() {
        this.head = new Block(null, null);
        this.size = 0;
    }

    /**
     * Creates a new block from the given transactions and appends it to the chain.
     *
     * The new block's previous pointer should reference the current head block
     * (or null if this is the first block in the chain).
     *
     * @param transactions the list of transactions to include in the new block
     */
    public void append(List<Transaction> transactions) {
        Block newBlock;
        if (this.head != null) {
            newBlock = new Block(transactions, this.head);
        } else {
            newBlock = new Block(transactions, null);
        }
        this.head = newBlock;
        this.size += 1;
    }

    /**
     * Returns the most recent block in the chain, or null if the chain is empty.
     *
     * @return the head block
     */
    public Block getHead() {
        return this.head;
    }

    /**
     * Returns the number of blocks in the chain.
     *
     * @return the chain length
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Validates the integrity of the entire chain.
     *
     * @return true if the entire chain is intact, false if any block
     *         has been tampered with
     */
    //TODO: fix
    public boolean validateChain() {
        String currentHash = this.head.getHashPointer().calculateHash();
        String expectedHash = this.head.getHashPointer().getHash();
        if (!currentHash.equals(expectedHash)) {
            return false;
        }

        return true;
    }
}
