package domain.usecases;

import domain.entities.CipherOptions;
import domain.entities.CryptoParams;
import domain.entities.CustomFile;

import java.nio.ByteBuffer;

import static domain.usecases.GenerateAesSecrestKey.SALT_SIZE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

public class EncryptContentFile {

    public static final int IV_SIZE = 12;

    private GenerateNewContent generateNewContent;

    public EncryptContentFile(GenerateNewContent generateNewContent) {
        this.generateNewContent = generateNewContent;
    }

    CustomFile encrypt(CustomFile file, CryptoParams params) throws Exception {
            byte[] iv = GenerateRandomBytes.withSizeOf(IV_SIZE);
            byte[] salt = params.password.length > 0 ? GenerateRandomBytes.withSizeOf(SALT_SIZE) : new byte[0];
            byte[] content = generateNewContent.handle(file.content, params, CipherOptions.of(ENCRYPT_MODE, iv, salt));
            return new CustomFile(file.name, addIvAndSaltInResultContent(iv, salt, content));
    }

    private byte[] addIvAndSaltInResultContent(byte[] iv, byte[] salt, byte[] content) {
        ByteBuffer allocate = ByteBuffer.allocate(iv.length + salt.length + content.length);
        return allocate
                .put(iv)
                .put(salt)
                .put(content)
                .array();
    }
}
