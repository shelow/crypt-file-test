package domain.usecases;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class GenerateAesSecrestKey {

    public static final int KEYSIZE = 256;
    public static final String AES = "AES";

    public static SecretKey fromStrKey(byte[] secretKey) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(AES);
        keyGen.init(KEYSIZE, SecureRandom.getInstanceStrong());
        SecretKey key = new SecretKeySpec(secretKey, AES);
        return key;
    }
}
