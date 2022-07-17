package unit.adapters.gateway;

import fr.salim.equisign.domain.ports.gateway.SecurityGateway;
import fr.salim.equisign.domain.usecases.GenerateRandomBytes;

public class InMemorySecurityGateway implements SecurityGateway {

    public static final int KEY_SIZE = 256 / 8;
    final byte[] secretKey;

    public InMemorySecurityGateway() {
        secretKey = generateRandomKey();
    }

    private byte[] generateRandomKey() {
        return GenerateRandomBytes.withSizeOf(KEY_SIZE);
    }

    public byte[] loadSecretKey() {
        return secretKey;
    }
}
