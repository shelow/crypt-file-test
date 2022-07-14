package domain.entities;

public class CipherOptions {
    public final int mode;
    public final byte[] iv;
    public final byte[] salt;

    public CipherOptions(int mode, byte[] iv, byte[] salt) {
        this.mode = mode;
        this.iv = iv;
        this.salt = salt;
    }

    public static CipherOptions of(int mode, byte[] iv, byte[] salt) {
        return new CipherOptions(mode, iv, salt);
    }
}
