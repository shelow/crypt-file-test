package domain.usecases;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

public class GenerateAesSecrestKey {

    public static final int KEYSIZE = 256;
    public static final String AES = "AES";
    public static final int SALT_SIZE = 16;
    public static final String PBKDF_2_WITH_HMAC_SHA_256 = "PBKDF2WithHmacSHA256";
    public static final int ITERATION_COUNT = 65536;

    public static SecretKey fromStrKey(byte[] secretKey) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(AES);
        keyGen.init(KEYSIZE, SecureRandom.getInstanceStrong());
        SecretKey key = new SecretKeySpec(secretKey, AES);
        return key;
    }


    public static SecretKey withPassword(char[] password) throws Exception {
        byte[] salt = GenerateRandomBytes.withSizeOf(SALT_SIZE);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF_2_WITH_HMAC_SHA_256);
        KeySpec spec = new PBEKeySpec(password, salt, ITERATION_COUNT, KEYSIZE);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), AES);
    }
}
