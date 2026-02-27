import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ByzantineAgreement {

    public static int DEFAULT = 0;

    // ---------------------------------------------------------------
    // Validity: if the general is honest, every honest party must
    // output exactly the general's input value v.
    // ---------------------------------------------------------------
    public static boolean validity(int v, List<Party> parties) {
        // TODO:
        if (!parties.get(0).isHonest) {
            return true;
        }
        for (int i = 0; i < parties.size(); i++) {
            if (!parties.get(i).isLeader && parties.get(i).isHonest) {
                if (parties.get(i).getOutput() != v) {
                    return false;
                }
            }
        }
        return true;
    }

    // ---------------------------------------------------------------
    // Agreement: all honest parties output the same value.
    // ---------------------------------------------------------------
    public static boolean agreement(List<Party> parties) {
        // TODO:
            boolean setoutput = false;
            int output = 0;
            for (int i = 0; i < parties.size(); i++) {
            if (!parties.get(i).isLeader && parties.get(i).isHonest) {
                if (!setoutput) {
                    output = parties.get(i).getOutput();
                    setoutput = true;
                } else {
                    if (parties.get(i).getOutput() != output) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // ---------------------------------------------------------------
    // Protocol - returns true iff validity and agreement are satisfied
    // ---------------------------------------------------------------
    public static boolean protocol(List<Party> parties, Map<Integer, Keys.PublicKey> PKI) {
        // TODO:
        // Round 1: The general sends the signed bit to all other parties.
        Party leader = null;

        for (int i = 0; i < parties.size(); i++) {
            if (parties.get(i).isLeader) {
                leader = parties.get(i);
                break;
            }
        }
        if (leader == null) {
            System.err.println("Error: General not found.");
            return false;
        }
        Message toSend = leader.sign(17);
        //round 1
        for (int i = 0; i < parties.size(); i++) {
            if (!parties.get(i).isLeader) {
                leader.send(parties.get(i),toSend, PKI);
            }
        }
        //round 2
        for (int i = 0; i < parties.size(); i++) {
            if (!parties.get(i).isLeader) {
                if (parties.get(i).getMsg().isEmpty()) {
                    System.out.println("Error: no message received by party " + i);
                    continue;
                }
                for (int j = 0; j < parties.size(); j++) {
                    if (!parties.get(i).isLeader && (j != i)){
                        parties.get(i).send(parties.get(j), parties.get(i).getMsg().get(0), PKI);
                    }
                }
            } else {
                if (!parties.get(i).isHonest) {
                Message fake = parties.get(i).sign(2);
                parties.get(i).send(parties.get(i+1), fake, PKI);
                }
            }
        }

        //decision time
        for (int i = 0; i < parties.size(); i++) {
            if (!parties.get(i).isLeader) {
                parties.get(i).decide();
            }
        }
        if (agreement(parties) && validity(toSend.value, parties)) {
            return true;
        }
        // The cases of the general being honest / dishonest should be handled in send.
        // Round 2: Relay (send) any messages recieved in round 1 to all other parties.
        // honest / dishonest behavior of non-general parties should also be handled in
        // send.
        // Decision Round.
        return false;
    }

    public static void main(String[] args) {
        int numParties = Integer.parseInt(args[0]);
        int numTraitors = Integer.parseInt(args[1]);
        boolean honestGeneral = Boolean.parseBoolean(args[2]);
        List<Party> PartyList = new ArrayList<Party>();
        Map<Integer, Keys.PublicKey> PKI = new HashMap<Integer,Keys.PublicKey>() {
            
        };

        PartyList.add(new Party(true, honestGeneral));
        PKI.put(0, PartyList.get(0).getPublicKey());
        for (int i = 1; i < numParties; i++) {
            Party newparty;
            if (i < numTraitors) {
               newparty = new Party(false, false);
            } else {
               newparty = new Party(false, true);
            }
            PartyList.add(newparty);
            PKI.put(i, newparty.getPublicKey());
        }
        boolean success = protocol(PartyList, PKI);
        System.out.println(success);

        // Test your protocol under various settings of n and t.
        // Test if validity and agreement are satisfied.
        // Since the dishonest send is non-deterministic, run your protocol **many**
        // times under each setting.
        // If you ever hit a case where validity and agreement are not met, you know
        // that it is not satisfied for the protocol.
        // However, seeing 1 run for which they ARE satisfied does not mean the protocol
        // satisfies BG.
        // Make sure to run a sufficient number of trials
    }

}
