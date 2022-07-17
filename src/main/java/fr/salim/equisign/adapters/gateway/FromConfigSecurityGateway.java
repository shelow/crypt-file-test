package fr.salim.equisign.adapters.gateway;

import fr.salim.equisign.domain.ports.gateway.SecurityGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class FromConfigSecurityGateway implements SecurityGateway {

    final byte[] secretKey;

    public FromConfigSecurityGateway(@Value("${secretKey}") String strSecretKey) {
        secretKey = strSecretKey.getBytes(StandardCharsets.UTF_8);
    }

    public byte[] loadSecretKey() {
        return secretKey;
    }
}
