import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

//used for getting data for tables. I assume this is what you wanted for the driver question(?)
public class tester{

public static void main(String[] args){
   try {
    int attempts = 0;
    byte[] message1;
    byte[] message2;
    byte[] hash1;
    long endTime;
    long startTime = System.nanoTime();

     while (attempts < 5000000) {
         message1 = Utils.genSalt();
         message2 = Utils.genSalt();
        if (Arrays.equals(message1, message2)){
            continue;
        }
        attempts ++;
        hash1 = Part1.computeDigest(message1, 1);
        if (Part1.verifyIntegrity(message2, hash1, 1)){
            endTime = System.nanoTime();
            System.out.println("Attempts: " + attempts + " Time " + (endTime-startTime)/1000000); 
            break;
        }
    }
    endTime = System.nanoTime();
    System.out.println("Attempts: " + attempts + " Time " + (endTime-startTime)/1000000); 
     
   
   
    startTime = System.nanoTime();
    message1 = Utils.genSalt();
    Commitment comt = Part2.commit(message1, 2);
    attempts = 0;
    while (attempts < 5000000) {
        attempts ++;
        message2 = Utils.genSalt();
        if (Part2.verify(comt, message2, 2)){
            endTime = System.nanoTime(); 
            for (int i = 0; i < 32; i++){
                System.out.print(message1[i]);
            }
            System.out.println();
             for (int i = 0; i < 32; i++){
                System.out.print(message2[i]);
            }
            System.out.println();

    System.out.println("Attempts: " + attempts + " Time " + (endTime-startTime)/1000000); 
            break;
        }
    }
    endTime = System.nanoTime();
    System.out.println("Attempts: " + attempts + " Time " + (endTime-startTime)/1000000); 



    message1 = Utils.genSalt();
    startTime = System.nanoTime();
    Part3.solvePuzzle(message1, 24);
    //had temporary statement in part3 to print attempts
    endTime = System.nanoTime();
    System.out.println("Time " + (endTime-startTime)/1000000); 
   }




      catch (NoSuchAlgorithmException e) {
            System.out.print("No Such Algorithm");
       }

}
}