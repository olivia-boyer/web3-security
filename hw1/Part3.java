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
    public static long solvePuzzle(byte[] puzzleID, int difficulty) throws NoSuchAlgorithmException {
        // TODO
        long x = 0;
        for (int i = 0; i < 10000000; i++) {
            //generate x
            if (verifyPuzzle(puzzleID, x, difficulty)){
                return x;
            }
            x++;
        }
            return -1;
       // throw new UnsupportedOperationException();
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
            throws NoSuchAlgorithmException {
   
        byte[] bytex = new byte[8];
        int i;
        int mask = 255;
        for (i = 7; i >= 0; i--) {
            bytex[i] = (byte) (x & mask);
            x = x >> 8;
        }
        byte[] concated = Utils.concat(puzzleID, bytex);
        concated = Part1.computeDigest(concated, 1);
        
       //byte[] concated = {0, (byte)0, 0, 0, 0};
        int bytenum = (difficulty / 8);
        for (i = 0; i < bytenum; i++){
            if (concated[i] != 0) {
                return false;
            } 
        }
        bytenum = difficulty % 8;
        int fin = concated[i];
        byte check = -128;
        for (i = 0; i < bytenum; i++) {
            if ((fin & check) != 0) {
                return false;
            }
                fin = fin << 1;
        }
        return true;

        
        //throw new UnsupportedOperationException();
    }
}
