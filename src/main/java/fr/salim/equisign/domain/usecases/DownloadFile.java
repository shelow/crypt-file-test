package fr.salim.equisign.domain.usecases;

import fr.salim.equisign.domain.entities.CustomFile;
import fr.salim.equisign.domain.exceptions.AccessDeniedException;
import fr.salim.equisign.domain.exceptions.NotFoundException;
import fr.salim.equisign.domain.ports.gateway.FileSystemGateway;
import fr.salim.equisign.domain.ports.repository.FileMetadataRepository;
import fr.salim.equisign.domain.entities.FileMetadata;
import fr.salim.equisign.domain.values.STR;

import java.util.Optional;

public class DownloadFile {
    private FileSystemGateway fileSystemGateway;
    private FileMetadataRepository fileMetadataRepository;
    private DecryptContentFile decryptContentFile;

    public DownloadFile(FileSystemGateway fileSystemGateway, FileMetadataRepository fileMetadataRepository, DecryptContentFile decryptContentFile) {
        this.fileSystemGateway = fileSystemGateway;
        this.fileMetadataRepository = fileMetadataRepository;
        this.decryptContentFile = decryptContentFile;
    }

    public CustomFile handle(String fileName, String password) {
        FileMetadata metadata = findMetadataFromRepos(fileName);
        CustomFile customFile = getCustomFromFileSystem(fileName);
        return metadata.encrypted ? decryptContent(customFile, password) : customFile;
    }

    private CustomFile decryptContent(CustomFile customFile, String password) {
        try {
            return STR.isEmpty(password)
                    ? decryptContentFile.decrypt(customFile)
                    : decryptContentFile.decryptWithPassword(customFile, password);
        } catch (Exception exception) {
            throw new AccessDeniedException();
        }
    }

    private FileMetadata findMetadataFromRepos(String fileName) {
        Optional<FileMetadata> foundMetadata = fileMetadataRepository.findByName(fileName);
        if (foundMetadata.isEmpty()) throw new NotFoundException("Le fichier " + fileName + " n'existe pas");
        return foundMetadata.get();
    }

    private CustomFile getCustomFromFileSystem(String fileName) {
        try {
            return fileSystemGateway.read(fileName).orElseThrow();
        } catch (Exception e){
            throw new NotFoundException("Le fichier "+fileName+" n'a pas pu être récupéré sur le disque");
        }
    }
}
