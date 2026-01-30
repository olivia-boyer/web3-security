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
        throw new UnsupportedOperationException("TODO");
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
        throw new UnsupportedOperationException("TODO");
    }
}
