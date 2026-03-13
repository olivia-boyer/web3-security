import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Random;

public class DSParty {
    private Random rand = new Random();
    public final int partyId; // 1-indexed; party 1 is the leader
    public final boolean isLeader;
    public final boolean isHonest;
    private final Wallet wallet;

    public final List<DSMessage> msgs;
    public Integer output;

    public DSParty(int partyId, boolean isLeader, boolean isHonest) {
        this.partyId = partyId;
        this.isLeader = isLeader;
        this.isHonest = isHonest;
        this.wallet = new Wallet(512);
        this.msgs = new ArrayList<>();
        this.output = null;
    }

    public Keys.PublicKey getPublicKey() {
        return wallet.getPublicKey();
    }

    // done
    public DSMessage.Signature sign(DSMessage msg) {
        byte[] val = Utils.int2byte(msg.value);
        byte[] toSign = val;
        for (int i = 0; i < msg.chainLength(); i++) {
            toSign = Utils.concat(toSign, msg.getSignatures().get(i).bytes);
        }
        toSign = wallet.sign(toSign);

        DSMessage.Signature newsig = new DSMessage.Signature(partyId, toSign);
        //msg.addSig(newsig);
        return newsig;
        // TODO: call sign on the concatenation of the value and all existing signatures
        // on the chain.

    }

    public void send(DSParty receiver, DSMessage msg, int roundNum, Map<Integer, Keys.PublicKey> PKI) {
        if (!isHonest) { // add more advanced one later
            int fakeval = ~msg.value;
            DSMessage fakemsg = new DSMessage(fakeval);
            receiver.receive(msg.addSig(sign(fakemsg)), roundNum, PKI);
            return;
        }
        receiver.receive(msg.addSig(sign(msg)), roundNum, PKI);
    }

    public void receive(DSMessage msg, int roundNum, Map<Integer, Keys.PublicKey> PKI) {
      //  System.out.println("receiving");
       // System.out.println(msg.chainLength());
        if (msg.chainLength() == roundNum) {
           // System.out.println("chain Length passes");

            List<DSMessage.Signature> sigs = msg.getSignatures();

            byte[] toSign = Utils.int2byte(msg.value);

            if (Wallet.verify(PKI.get(1), toSign, sigs.get(0).bytes)) {
               // System.out.println("signed by leader");
                toSign = Utils.concat(toSign, sigs.get(0).bytes);
                boolean valid = true;
                for (int i = 1; i < msg.chainLength(); i++) {
                    valid = Wallet.verify(PKI.get(sigs.get(i).partyId), toSign, sigs.get(i).bytes);
                    if (!valid) {
                        System.out.println("invalid signature from other party");
                        return;
                    }
                    toSign = Utils.concat(toSign, sigs.get(i).bytes);
                }
               // System.out.println("adding message");
                msgs.add(msg);
            }
        }
        // TODO
    }

    public void relay(List<DSParty> allParties, int roundNum, Map<Integer, Keys.PublicKey> PKI) {
        if (isHonest) {
            int i = msgs.size() - 1;
               // sign(msgs.get(i));
               if (i >= 0) {
                for (int j = 0; j < allParties.size(); j++) {
                    send(allParties.get(j), msgs.get(i), roundNum, PKI);
            }
        }
            // TODO: for each message in msgs, add your signature and send to all
        } else {
            // TODO: implement dishonest relay behavior
        }
    }

    public void decide() {

        if (isHonest) {
            if (msgs.isEmpty()) {
                this.output = DolevStrong.DEFAULT;
                return;
            }
            int val = msgs.get(0).value;
            for (int i = 0; i < msgs.size(); i++) {
                if (val != msgs.get(i).value) {
                    this.output = DolevStrong.DEFAULT; // default value
                    return;
                }
            }
            this.output = val;
            return;
        } else {
            if (!msgs.isEmpty()){
            int val = msgs.get(0).value;
            output = ~val;
            } else {
                output = 0;
            }
            return;
        }
    }
}
