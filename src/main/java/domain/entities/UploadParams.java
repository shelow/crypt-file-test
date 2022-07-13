package domain.entities;

import domain.exceptions.MissingPasswordException;

public class UploadParams {
    public final boolean shouldEncryptFile;
    public final char[] password;

    public UploadParams(boolean shouldEncryptFile, char[] password) {
        this.shouldEncryptFile = shouldEncryptFile;
        this.password = password;
    }

    public static UploadParams of(boolean encryptFile, String password) {
        if(password == null || password.isEmpty()) throw new MissingPasswordException();
        return new UploadParams(encryptFile, password.toCharArray());
    }

    public static UploadParams of(boolean encryptFile) {
        return new UploadParams(encryptFile, new char[0]);
    }
}
