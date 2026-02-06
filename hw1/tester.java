import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

public class tester{

public static void main(String[] args){
    byte[] message = Utils.genSalt();
    try {
    byte[] hash1 = Part1.computeDigest(message, 1);
    System.out.println(Part1.verifyIntegrity(message, hash1, 1));
    
        Commitment comt = Part2.commit(message, 1);
    System.out.println(Part2.verify(comt, message, 1));

    System.out.println(Part3.solvePuzzle(message, 5));
    System.out.println(Part3.verifyPuzzle(message, 0, 0));
    }
      catch (NoSuchAlgorithmException e) {
            System.out.print("No Such Algorithm");
       }

}
}