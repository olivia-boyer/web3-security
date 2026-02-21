import java.io.*;
import java.util.*;

public class Transaction {


    public static class Input {
        public final byte[]    prevTxHash;
        public final int       prevOutIndex;
        public       byte[]    signature;

        public Input(byte[] prevTxHash, int prevOutIndex) {
            this.prevTxHash   = prevTxHash;
            this.prevOutIndex = prevOutIndex;
        }

        @Override
    public boolean equals(Object input) {
        Input i = (Input) input;
        if (Arrays.equals(this.prevTxHash, i.prevTxHash)) {
            if (this.prevOutIndex == i.prevOutIndex) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.serialize());
    }


        public byte[] serialize() {
            byte[] bytex = new byte[4];
            int mask = 255;
            int out = this.prevOutIndex;
            for (int i = 3; i >= 0; i--) {
                bytex[i] = (byte) (out & mask);
                out = out >> 8;
        }
        byte[] concated = Utils.concat(this.prevTxHash, bytex);
        return concated;
        }
    }

    public static class Output {
        public final double    value;
        public final Keys.PublicKey recipient;

        public Output(double value, Keys.PublicKey recipient) {
            this.value     = value;
            this.recipient = recipient;
        }

        public byte[] serialize() {
            long bits = Double.doubleToLongBits(value);
 byte[] bytex = new byte[8];
            int mask = 255;
            long out = bits;
            for (int i = 7; i >= 0; i--) {
                bytex[i] = (byte) (out & mask);
                out = out >> 8;
        }
        bytex = Utils.concat(bytex, recipient.e.toByteArray(), recipient.n.toByteArray());
        return bytex;
        }
    }

    private final List<Input>  inputs = new ArrayList<Input>();
    private final List<Output> outputs = new ArrayList<Output>();
    private byte[] hash;

    public Transaction() {}

    public List<Output> geOutputs() {
        return this.outputs;
    }

    public List<Input> getInputs() {
        return this.inputs;
    }

    public void addInput(byte[] prevTxHash, int prevOutIndex) {
        Input newinput = new Input(prevTxHash, prevOutIndex);
        inputs.add(newinput);
        return;
    }

    public void addOutput(double value, Keys.PublicKey recipient) {
        Output newOutput = new Output(value, recipient);
        outputs.add(newOutput);
        return;
    }

    public byte[] getInputDataToSign(int inputIndex) throws IOException {
        return inputs.get(inputIndex).serialize();
    }

    public void signInput(int inputIndex, byte[] signature) {
        this.inputs.get(inputIndex).signature = signature;
    }

    public void computeHash() throws Exception {
        byte[] tohash = new byte[0];
        for (int i = 0; i < this.inputs.size(); i++) {
            tohash = Utils.concat(tohash, inputs.get(i).serialize());
        }
        for (int i = 0; i < this.outputs.size(); i++) {
            tohash = Utils.concat(tohash, outputs.get(i).serialize());
        }

        //make byte array that concatenates all serialized inputs + all serialized outputs 
        this.hash = Utils.sha256(tohash);
        return;
    }
    
    public byte[] getHash() {
        return this.hash;
    }

}
