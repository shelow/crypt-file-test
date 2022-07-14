package domain.values;

public class CryptoParams {
    public final boolean shouldEncryptFile;
    public final char[] password;

    public CryptoParams(boolean shouldEncryptFile, char[] password) {
        this.shouldEncryptFile = shouldEncryptFile;
        this.password = password;
    }

    public static CryptoParams of(boolean encryptFile, String password) {
        return new CryptoParams(encryptFile, STR.isEmpty(password) ? new char[0] : password.toCharArray());
    }

}
