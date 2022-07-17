package fr.salim.equisign.domain.usecases;

import java.util.concurrent.ThreadLocalRandom;

public class GenerateRandomBytes {
    public static byte[] withSizeOf(int size) {
        byte[] randomBytes = new byte[size];
        ThreadLocalRandom.current().nextBytes(randomBytes);
        return randomBytes;
    }
}
