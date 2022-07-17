package fr.salim.equisign.adapters;

import fr.salim.equisign.domain.ports.gateway.FileSystemGateway;
import fr.salim.equisign.domain.ports.gateway.SecurityGateway;
import fr.salim.equisign.domain.ports.repository.FileMetadataRepository;
import fr.salim.equisign.domain.usecases.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBeansAdapters {

    @Bean
    public GenerateNewContent generateNewContent(SecurityGateway securityGateway){
        return new GenerateNewContent(securityGateway);
    }

    @Bean
    public EncryptContentFile encryptContentFile(GenerateNewContent generateNewContent) {
        return new EncryptContentFile(generateNewContent);
    }

    @Bean
    public DecryptContentFile decryptContentFile(GenerateNewContent generateNewContent){
        return new DecryptContentFile(generateNewContent);
    }

    @Bean
    public UploadFile uploadFile(
            FileSystemGateway localFileSystemGateway,
            EncryptContentFile encryptContentFile,
            FileMetadataRepository metadataRepos){
        return new UploadFile(localFileSystemGateway, encryptContentFile, metadataRepos);
    }

    @Bean
    public DownloadFile downloadFile(
            FileSystemGateway localFileSystemGateway,
            FileMetadataRepository metadataRepos,
            DecryptContentFile decryptContentFile){
        return new DownloadFile(localFileSystemGateway, metadataRepos, decryptContentFile);
    }

}
