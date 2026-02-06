import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

public class Part3 {
    // -----------------------------
    // Part 3: Puzzle Friendliness
    // -----------------------------
    
    /**
     * Find a solution x (nonce) such that H(puzzleID || x) ∈ Y,
     * where Y is the set of all hashes with at least 'difficulty' leading zero bits.
     * 
     * @param puzzleID the puzzle identifier (arbitrary data - high min-entropy value)
     * @param difficulty number of leading zero bits required (defines |Y| = 2^(256-difficulty))
     * @return a nonce x that solves the puzzle, or -1 if not found within reasonable attempts
     */
    public static long solvePuzzle(byte[] puzzleID, int difficulty) throws Exception {
        // TODO
        long x = 0;
        for (int i = 0; i < 1000; i++) {
            //generate x
            if (verifyPuzzle(puzzleID, x, difficulty)){
                return x;
            }
            x++;
        }
            return -1;
       // throw new UnsupportedOperationException("TODO");
    }
    
    /**
     * Verify that x is a valid solution: H(puzzleID || x) ∈ Y.
     * 
     * @param puzzleID the puzzle identifier
     * @param x the proposed solution (nonce)
     * @param difficulty required number of leading zero bits (defines Y)
     * @return true if H(puzzleID || x) has at least 'difficulty' leading zeros
     */
    public static boolean verifyPuzzle(byte[] puzzleID, long x, int difficulty) 
            throws Exception {
        // TODO
        Long xlong = x;
        String xstring = xlong.toString();
        byte[] concated = Utils.concat(puzzleID, xstring.getBytes());
        Part1.computeDigest(concated, 1);
        int bytenum = difficulty / 8;
        int i;
        for (i = 0; i < bytenum; i++){
            if (concated[i] != 0) {
                return false;
            }
        }
        bytenum = difficulty % 8;
        return  concated[i] < (int)(Math.pow(2, bytenum));

        
        //throw new UnsupportedOperationException("TODO");
    }
}
