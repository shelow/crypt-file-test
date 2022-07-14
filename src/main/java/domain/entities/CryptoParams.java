package domain.entities;

import domain.exceptions.MissingPasswordException;

public class CryptoParams {
    public final boolean shouldEncryptFile;
    public final char[] password;

    public CryptoParams(boolean shouldEncryptFile, char[] password) {
        this.shouldEncryptFile = shouldEncryptFile;
        this.password = password;
    }

    public static CryptoParams of(boolean encryptFile, String password) {
        if(password == null || password.isEmpty()) throw new MissingPasswordException();
        return new CryptoParams(encryptFile, password.toCharArray());
    }

    public static CryptoParams of(boolean encryptFile) {
        return new CryptoParams(encryptFile, new char[0]);
    }
}
