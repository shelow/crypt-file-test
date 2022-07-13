package domain.usecases;

import domain.entities.CustomFile;
import domain.ports.gateway.SecurityGateway;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import static javax.crypto.Cipher.ENCRYPT_MODE;

public class EncryptContentFile {

    public static final int IV_SIZE = 12;
    private static final int AUTH_TAG_SIZE = 128;
    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";

    private SecurityGateway securityGateway;

    public EncryptContentFile(SecurityGateway securityGateway) {
        this.securityGateway = securityGateway;
    }

    public CustomFile createNewEncryptContentFile(CustomFile file) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = createCipher();
        byte[] encryptedText = cipher.doFinal(file.fileContent);
        return new CustomFile(file.name, encryptedText);
    }

    private Cipher createCipher() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        SecretKey key = getSecretKey();
        byte[] iv = GenerateRandomBits.withSizeOf(IV_SIZE);
        return getConfiguratedCipher(key, iv);
    }

    private SecretKey getSecretKey() throws NoSuchAlgorithmException {
        byte[] strKey = securityGateway.loadSecretKey();
        SecretKey key = generateSecrateKeyFromStrKey(strKey);
        return key;
    }

    private Cipher getConfiguratedCipher(SecretKey key, byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
        AlgorithmParameterSpec gcmParameterSpec = new GCMParameterSpec(AUTH_TAG_SIZE, iv);
        cipher.init(ENCRYPT_MODE, key, gcmParameterSpec);
        return cipher;
    }

    private SecretKey generateSecrateKeyFromStrKey(byte[] secretKey) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256, SecureRandom.getInstanceStrong());
        SecretKey key = new SecretKeySpec(secretKey, "AES");
        return key;
    }
}
