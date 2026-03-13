import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class Part2 {

    // -----------------------------
    // Part 2: Commitments
    // -----------------------------
    /** 
     * @param message the message to commit
     * @param hashFunction the hash function to use
     * 
     * @return a commitment type with hashed message and salt
     */

    public static Commitment commit(byte[] message, int hashFunction) throws NoSuchAlgorithmException {
        
        byte[] r = Utils.genSalt();
        byte[] concated = Utils.concat(r, message);
        byte[] c = Part1.computeDigest(concated, hashFunction);
        Commitment retVal = new Commitment(c,r);
        return retVal;
    
    }

    /** 
     * @param c the commitment to check against
     * @param message the message to be compared to the commitment
     * @param hashFunction the hash function to use
     * 
     * @return whether the hash of message matches the commitment
     */
    public static boolean verify(Commitment c, byte[] message, int hashFunction) throws NoSuchAlgorithmException {
        byte[] rVal = c.getSalt();
        byte[] concated = Utils.concat(rVal, message);
        return Part1.verifyIntegrity(concated, c.getCom(), hashFunction);
    }

}
