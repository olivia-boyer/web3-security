import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
//import Utils.java;

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
     *                     2 = SHA-256 truncated to 8 bits
     *                     3 = SHA-256 truncated to 16 bits
     *                     other = print error and return null
     * @return the message digest as a byte array
     */
    public static byte[] computeDigest(byte[] message, int hashFunction)  throws NoSuchAlgorithmException {
       if (hashFunction == 2){
        return Utils.hashTruncated(message, 8);
       } 
       if (hashFunction == 1) {
        return Utils.sha256(message);
       }
       if (hashFunction == 3) {
        return Utils.hashTruncated(message, 16);
       }
       

       System.out.print("Error: int hashFunction is out of range 1-3");
       return null;
       
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
                                          int hashFunction) throws NoSuchAlgorithmException {
        byte[] hashedMsg = computeDigest(message, hashFunction);
        for (int i = 0; i < hashedMsg.length; i++){
            if (hashedMsg[i] != expectedDigest[i]){
                return false;
            }
        }
        return true;
    }
    
}
//build tester for 1b