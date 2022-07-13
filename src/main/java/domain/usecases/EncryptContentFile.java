package domain.usecases;

import domain.entities.CustomFile;
import domain.entities.UploadParams;
import domain.ports.gateway.SecurityGateway;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class EncryptContentFile {

    public static final int IV_SIZE = 12;

    private SecurityGateway securityGateway;

    public EncryptContentFile(SecurityGateway securityGateway) {
        this.securityGateway = securityGateway;
    }

    public CustomFile createNewEncryptContentFile(CustomFile file, UploadParams params) throws Exception {
        Cipher cipher = createCipher(params);
        byte[] encryptedText = cipher.doFinal(file.content);
        return new CustomFile(file.name, encryptedText);
    }

    private Cipher createCipher(UploadParams params) throws Exception {
        byte[] iv = GenerateRandomBytes.withSizeOf(IV_SIZE);
        SecretKey secretKey = getSecretKey(params.password);
        return CreateAesGcmCipherInstance.getConfigurated(secretKey, iv);
    }

    private SecretKey getSecretKey(char[] password) throws Exception {
        byte[] strKey = securityGateway.loadSecretKey();
        return password.length == 0
                ? GenerateAesSecrestKey.fromStrKey(strKey)
                : GenerateAesSecrestKey.withPassword(password);
    }
}
