package domain.usecases;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import static javax.crypto.Cipher.ENCRYPT_MODE;

public class CreateAesGcmCipherInstance {

    private static final int AUTH_TAG_SIZE = 128;
    private static final String AES_GCM_ALGO = "AES/GCM/NoPadding";

    public static Cipher getConfigurated(SecretKey key, byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(AES_GCM_ALGO);
        AlgorithmParameterSpec gcmParameterSpec = new GCMParameterSpec(AUTH_TAG_SIZE, iv);
        cipher.init(ENCRYPT_MODE, key, gcmParameterSpec);
        return cipher;
    }
}
