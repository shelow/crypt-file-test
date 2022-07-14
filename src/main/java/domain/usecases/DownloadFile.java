package domain.usecases;

import domain.entities.CustomFile;
import domain.exceptions.AccessDeniedException;
import domain.exceptions.NotFoundException;
import domain.ports.gateway.FileSystemGateway;
import domain.ports.repository.FileMetadaRepository;
import domain.values.FileMetadata;
import domain.values.STR;

import java.util.Optional;

public class DownloadFile {
    private FileSystemGateway fileSystemGateway;
    private FileMetadaRepository fileMetadaRepository;
    private DecryptContentFile decryptContentFile;

    public DownloadFile(FileSystemGateway fileSystemGateway, FileMetadaRepository fileMetadaRepository, DecryptContentFile decryptContentFile) {
        this.fileSystemGateway = fileSystemGateway;
        this.fileMetadaRepository = fileMetadaRepository;
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
        Optional<FileMetadata> foundMetadata = fileMetadaRepository.findByName(fileName);
        if (foundMetadata.isEmpty()) throw new NotFoundException("Le fichier " + fileName + " n'existe pas");
        return foundMetadata.get();
    }

    private CustomFile getCustomFromFileSystem(String fileName) {
        return fileSystemGateway.read(fileName).orElseThrow();
    }
}
