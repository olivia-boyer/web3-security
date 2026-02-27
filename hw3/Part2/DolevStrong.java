import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DolevStrong {
    public static final int DEFAULT = 0;

    // ---------------------------------------------------------------
    // Validity: if the general is honest, every honest party outputs
    //           exactly the general's input value v.
    // ---------------------------------------------------------------
    public static boolean validity(int v, List<DSParty> parties) {
        if (!parties.get(0).isHonest) {
            return true;
        }
        for (int i = 0; i < parties.size(); i++) {
            if (!parties.get(i).isLeader && parties.get(i).isHonest) {
                if (parties.get(i).output != v) {
                    return false;
                }
            }
        }
        return true;
    }

    // ---------------------------------------------------------------
    // Agreement: all honest parties output the same value.
    // ---------------------------------------------------------------
    public static boolean agreement(List<DSParty> parties) {
        boolean setoutput = false;
            int output = 0;
            for (int i = 0; i < parties.size(); i++) {
            if (!parties.get(i).isLeader && parties.get(i).isHonest) {
                if (!setoutput) {
                    output = parties.get(i).output;
                    setoutput = true;
                } else {
                    if (parties.get(i).output != output) {
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
    public static boolean protocol(List<DSParty> parties, int t, Map<Integer, Keys.PublicKey> PKI) {
        // TODO:
        // Round 1: leader sends its signed message to all other parties
           DSParty leader = null;

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
        DSMessage toSend = new DSMessage(17);
        leader.sign(toSend);
        //round 1
        //System.out.println("round 1 begins");
        for (int i = 0; i < parties.size(); i++) {
            if (!parties.get(i).isLeader) {
                leader.send(parties.get(i),toSend, 1, PKI);
            }
        }
        // Rounds 2 through t+1: all non-leader parties call relay()
        for (int i = 2; i < t+1; i++) {
            for (int j = 1; j < parties.size(); j++) {
                parties.get(j).relay(parties, i, PKI);
            }
        }
        // Decision round.
               for (int i = 0; i < parties.size(); i++) {
            if (!parties.get(i).isLeader) {
                parties.get(i).decide();
            }
        }
        if (agreement(parties) && validity(toSend.value, parties)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
              int numParties = Integer.parseInt(args[0]);
        int numTraitors = Integer.parseInt(args[1]);
        boolean honestGeneral = Boolean.parseBoolean(args[2]);
        List<DSParty> PartyList = new ArrayList<DSParty>();
        Map<Integer, Keys.PublicKey> PKI = new HashMap<Integer,Keys.PublicKey>() {
            
        };

        PartyList.add(new DSParty(1, true, honestGeneral));
        PKI.put(1, PartyList.get(0).getPublicKey());
        for (int i = 1; i < numParties; i++) {
            DSParty newparty;
            if (i < numTraitors) {
               newparty = new DSParty(i+1, false, false);
            } else {
               newparty = new DSParty(i+1, false, true);
            }
            PartyList.add(newparty);
            PKI.put(i+1, newparty.getPublicKey());
        }
        boolean success = protocol(PartyList, PartyList.size(), PKI);
        System.out.println(success);
        // Test your protocol under various settings of n and t.
        // Run many trials per setting (dishonest behavior is non-deterministic).
        // Record your results in the README table.
    }
}
