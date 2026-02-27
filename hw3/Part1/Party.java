import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Party {

    public final boolean  isLeader;
    public final boolean  isHonest;

    private Random rand = new Random();

    private final Wallet  wallet;      
    private  final List<Message> msgs;  // V_i
    private Integer output;            // set during decision phase (decide)

    public Party(boolean isLeader, boolean isHonest) {
        this.isLeader = isLeader;
        this.isHonest = isHonest;
        this.wallet   = new Wallet(512); 
        this.msgs     = new ArrayList<>();
        this.output   = null;
    }

    public Keys.PublicKey getPublicKey() {
        return wallet.getPublicKey();
    }
    public List<Message> getMsg() {
        return this.msgs;
    }
    public Integer getOutput() {
        return this.output;
    }

    public Message sign(int v) {
        byte[] msg = Utils.int2byte(v);
        return new Message(v, wallet.sign(msg));
    }

    public void send(Party receiver, Message msg, Map<Integer, Keys.PublicKey> PKI) {
        if (!isHonest) {
            int doublesend = rand.nextInt() % 20;
            int fakeval = ~msg.value;
            Message fakemsg =  this.sign(fakeval);
            receiver.receive(fakemsg, PKI);
            if (doublesend == 0) {
            //    System.out.println("doublesending");
                receiver.receive(msg, PKI);
            }
            // TODO: Implement a dishonest send protocol.
            //       Think adversarially... a faulty party can deviate from the protocol in any way
            return;
        }

        receiver.receive(msg, PKI);
    }

    
    public void receive(Message msg, Map<Integer, Keys.PublicKey> PKI) {
        byte[] msgval = Utils.int2byte(msg.value);
        if (Wallet.verify(PKI.get(0), msgval, msg.sig)) {
            msgs.add(msg);
        }
        return;
        //TODO: validate that the message was signed by the general and add it to V_i if so
    }

    public void decide() {
        if (isHonest) {
            int val = msgs.get(0).value;
            for (int i = 0; i < msgs.size(); i++) {
                if (val != msgs.get(i).value) {
                    this.output = 0; //default value
                    return;
                }
            }
            this.output = val;
            return;
        } else {
            int val = msgs.get(0).value;
            output = ~val;
            return;
        }
    }
}
