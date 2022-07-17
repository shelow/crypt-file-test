package fr.salim.equisign.domain.usecases;

import fr.salim.equisign.domain.values.CipherOptions;
import fr.salim.equisign.domain.values.CryptoParams;
import fr.salim.equisign.domain.ports.gateway.SecurityGateway;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class GenerateNewContent {

    private SecurityGateway securityGateway;

    public GenerateNewContent(SecurityGateway securityGateway) {
        this.securityGateway = securityGateway;
    }

    public byte[] handle(byte[] inputContent, CryptoParams params, CipherOptions options) throws Exception {
        Cipher cipher = createCipher(params, options);
        return cipher.doFinal(inputContent);
    }

    private Cipher createCipher(CryptoParams params, CipherOptions options) throws Exception {
        SecretKey secretKey = generateSecretKey(params.password, options);
        return CreateAesGcmCipherInstance.getConfigurated(secretKey, options);
    }

    private SecretKey generateSecretKey(char[] password, CipherOptions options) throws Exception {
        byte[] strKey = securityGateway.loadSecretKey();
        return password.length == 0
                ? GenerateAesSecrestKey.fromStrKey(strKey)
                : GenerateAesSecrestKey.withPassword(password, options);
    }
}
