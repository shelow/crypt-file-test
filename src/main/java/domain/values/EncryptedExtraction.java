package domain.values;

import domain.usecases.DecryptContentFile;
import domain.usecases.GenerateAesSecrestKey;

import java.nio.ByteBuffer;

public class EncryptedExtraction {

    public final byte[] iv;
    public final byte[] content;
    public final byte[] salt;

    public EncryptedExtraction(ByteBuffer bb, boolean withPassword) {
        iv = getBytesFromBuffer(bb, DecryptContentFile.IV_SIZE);
        salt = withPassword ? getBytesFromBuffer(bb, GenerateAesSecrestKey.SALT_SIZE) : new byte[0];
        content = getBytesFromBuffer(bb, bb.remaining());
    }

    private byte[] getBytesFromBuffer(ByteBuffer bb, int nbByte) {
        byte[] cipherContent = new byte[nbByte];
        bb.get(cipherContent);
        return cipherContent;
    }
}
