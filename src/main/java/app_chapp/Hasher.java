package app_chapp;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Igor Konovalov on 19.11.2017.
 */
public class Hasher {

    /*
    These method uses security Digest with MD5 algorithm
     */
    public String hash(String whatToHash) throws NoSuchAlgorithmException {
        String plaintext = whatToHash;
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(plaintext.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);

        // Now we need to zero pad it if you actually want the full 32 chars.
        while(hashtext.length() < 32 ){
            hashtext = "0" + hashtext;
        }

        return hashtext;
    }
}
