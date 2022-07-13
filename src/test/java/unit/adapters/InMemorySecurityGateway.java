package unit.adapters;

import domain.ports.gateway.SecurityGateway;
import domain.usecases.GenerateRandomBits;

public class InMemorySecurityGateway implements SecurityGateway {

    public static final int KEY_SIZE = 256 / 8;
    final byte[] secretKey;

    public InMemorySecurityGateway() {
        secretKey = generateRandomKey();
    }

    private byte[] generateRandomKey() {
        return GenerateRandomBits.withSizeOf(KEY_SIZE);
    }

    public byte[] loadSecretKey() {
        return secretKey;
    }
}