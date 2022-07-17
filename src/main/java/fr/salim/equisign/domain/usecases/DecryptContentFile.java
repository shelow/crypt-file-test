package fr.salim.equisign.domain.usecases;

import fr.salim.equisign.domain.entities.CustomFile;
import fr.salim.equisign.domain.values.CipherOptions;
import fr.salim.equisign.domain.values.CryptoParams;
import fr.salim.equisign.domain.values.EncryptedExtraction;

import java.nio.ByteBuffer;

import static fr.salim.equisign.domain.values.STR.EMPTY;
import static javax.crypto.Cipher.DECRYPT_MODE;

public class DecryptContentFile {

    public static final int IV_SIZE = 12;

    private GenerateNewContent generateNewContent;

    public DecryptContentFile(GenerateNewContent generateNewContent) {
        this.generateNewContent = generateNewContent;
    }

    CustomFile decrypt(CustomFile customFile) throws Exception {
            EncryptedExtraction extraction = extractFromEncryptedContent(customFile, false);
            CipherOptions options = CipherOptions.of(DECRYPT_MODE, extraction.iv, extraction.salt);
            byte[] bytes = generateNewContent.handle(extraction.content, CryptoParams.of(true, EMPTY), options);
            return new CustomFile(customFile.name, bytes);
    }

    CustomFile decryptWithPassword(CustomFile customFile, String password) throws Exception {
            EncryptedExtraction extraction = extractFromEncryptedContent(customFile, true);
            CipherOptions options = CipherOptions.of(DECRYPT_MODE, extraction.iv, extraction.salt);
            byte[] bytes = generateNewContent.handle(extraction.content, CryptoParams.of(true, password), options);
            return new CustomFile(customFile.name, bytes);
    }

    private EncryptedExtraction extractFromEncryptedContent(CustomFile customFile, boolean withPassword) {
        ByteBuffer bb = ByteBuffer.wrap(customFile.content);
        return new EncryptedExtraction(bb, withPassword);
    }

}
