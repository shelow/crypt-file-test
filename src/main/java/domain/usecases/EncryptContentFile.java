package domain.usecases;

import domain.entities.CustomFile;
import domain.ports.gateway.SecurityGateway;

import javax.crypto.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class EncryptContentFile {

    public static final int IV_SIZE = 12;

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
        byte[] iv = GenerateRandomBytes.withSizeOf(IV_SIZE);
        return CreateAesGcmCipherInstance.getConfigurated(getSecretKey(), iv);
    }

    private SecretKey getSecretKey() throws NoSuchAlgorithmException {
        byte[] strKey = securityGateway.loadSecretKey();
        return GenerateAesSecrestKey.fromStrKey(strKey);
    }
}
