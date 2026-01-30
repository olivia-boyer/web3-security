import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

public class Part1 {
    // -----------------------------
    // Part 1: Message Digests
    // -----------------------------
    
    /**
     * Compute the message digest of a message using specified hash function.
     * 
     * @param message the message to hash
     * @param hashFunction which hash to use:
     *                     1 = full SHA-256 (256 bits)
     *                     2 = truncated to 8 bits
     *                     3 = truncated to 16 bits
     *                     other = print error and return null
     * @return the message digest as a byte array
     */
    public static byte[] computeDigest(byte[] message, int hashFunction) 
            throws NoSuchAlgorithmException {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
    
    /**
     * Verify that a message matches an expected digest.
     * 
     * @param message the message to verify
     * @param expectedDigest the expected digest
     * @param hashFunction which hash function to use (1, 2, or 3)
     * @return true if message's digest matches expectedDigest
     */
    public static boolean verifyIntegrity(byte[] message, byte[] expectedDigest, 
                                          int hashFunction) 
            throws NoSuchAlgorithmException {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
    
    /**
     * Attempt to find two different messages with the same digest.
     * 
     * @param hashFunction which hash to test (1, 2, or 3)
     * @return two different messages with same digest, or null if not found
     */
    public static String[] findCollision(int hashFunction) throws Exception {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
