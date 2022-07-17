package fr.salim.equisign.domain.usecases;

import fr.salim.equisign.domain.values.CipherOptions;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.spec.AlgorithmParameterSpec;

public class CreateAesGcmCipherInstance {

    private static final int AUTH_TAG_SIZE = 128;
    private static final String AES_GCM_ALGO = "AES/GCM/NoPadding";

    public static Cipher getConfigurated(SecretKey key, CipherOptions options) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_GCM_ALGO);
        AlgorithmParameterSpec gcmParameterSpec = new GCMParameterSpec(AUTH_TAG_SIZE, options.iv);
        cipher.init(options.mode, key, gcmParameterSpec);
        return cipher;
    }


}
