import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Wallet {

    private final Keys.PrivateKey privateKey;
    private final Keys.PublicKey  publicKey;

    public Wallet(int keySize) {
        privateKey = null;
        publicKey = null;
    }

    public Keys.PublicKey getPublicKey() {
        return this.publicKey;
    }

    public Keys.Key[] generateKeys(int keySize) {
        /*p = genrandom prime
        q = gen random prime
        n = p*q
        phin = (p-1)(q-1)
        bigint e = 65537
        d = e.modInverse(phin)
        keyset = keys.key[2]
        keyset[0] = privateKey(d, n)
        keyset[1] = publicKey(e, n)
        return keyset
        */
    }

    public byte[] sign(byte[] message) {
       /* h = hash(m)
       sig = h^(sk.d) mod sk.n
        return sig; 
        */
    }

    public static boolean verify(Keys.PublicKey pk, byte[] message, byte[] signature) {
       /* h = hash(m)
       
    }

}
