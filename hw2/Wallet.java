import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.ByteBuffer;

public class Wallet {

     private static final SecureRandom RNG = new SecureRandom();

    private final Keys.PrivateKey privateKey;
    private final Keys.PublicKey  publicKey;

        /** SHA-256 using Java standard library. */

    public Wallet(int keySize) {
        Keys.Key[] keyset = generateKeys(keySize);
        privateKey = (Keys.PrivateKey) keyset[0];
        publicKey = (Keys.PublicKey) keyset[1];
    }

    public Keys.PublicKey getPublicKey() {
        return this.publicKey;
    }

    public Keys.Key[] generateKeys(int keySize) {

        BigInteger p = new BigInteger(keySize,10,RNG);
        BigInteger q = new BigInteger(keySize,10,RNG); 
        BigInteger lilP = p.subtract(BigInteger.ONE);
        BigInteger lilQ = q.subtract(BigInteger.ONE);
        BigInteger n = p.multiply(q);
        BigInteger phin = (lilP).multiply(lilQ);
        BigInteger e = new BigInteger("65537");
        BigInteger d = e.modInverse(phin);
        Keys.Key[] keyset = new Keys.Key[2];
        keyset[0] = new Keys.PrivateKey(d, n);
        keyset[1] = new Keys.PublicKey(e, n);
        return keyset; 
    }

    public byte[] sign(byte[] message) {
        BigInteger h = new BigInteger(Utils.sha256(message));
        return (h.modPow(privateKey.d, privateKey.n)).toByteArray();
   
    }

    public static boolean verify(Keys.PublicKey pk, byte[] message, byte[] signature) {
        BigInteger h = new BigInteger(Utils.sha256(message));
        BigInteger sig = new BigInteger(signature);
        BigInteger se = sig.modPow(pk.e,pk.n);
        BigInteger hmod = h.mod(pk.n);
        return se.compareTo(hmod) == 0;
    }

}
