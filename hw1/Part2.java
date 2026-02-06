import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class Part2 {

    // -----------------------------
    // Part 2: Commitments
    // -----------------------------
    /** TODO: return (c, r) where r is random 32 bytes and c = SHA256(r || message). */
    public static Commitment commit(byte[] message, int hashFunction) throws NoSuchAlgorithmException {
        
        byte[] r = Utils.genSalt();
        byte[] concated = Utils.concat(r, message);
        byte[] c = Part1.computeDigest(concated, hashFunction);
        Commitment retVal = new Commitment(c,r);
        return retVal;
        // TODO 
        //throw new UnsupportedOperationException("TODO");
    }

    /** TODO: return true iff c.c equals SHA256(c.r || message). */
    public static boolean verify(Commitment c, byte[] message, int hashFunction) throws NoSuchAlgorithmException {
        byte[] rVal = c.getSalt();
        byte[] concated = Utils.concat(rVal, message);
        return Part1.verifyIntegrity(concated, c.getCom(), hashFunction);
    }

}
