package domain.usecases;

import domain.entities.CustomFile;
import domain.exceptions.AccessDeniedException;
import domain.exceptions.NotFoundException;
import domain.ports.gateway.FileSystemGateway;
import domain.ports.repository.FileMetadaRepository;
import domain.values.FileMetadata;

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

    public CustomFile handle(String fileName) {
        FileMetadata metadata = findMetadataFromRepos(fileName);
        CustomFile customFile = getCustomFromFileSystem(fileName);
        return metadata.encrypted ? decryptContent(customFile) : customFile;
    }

    private CustomFile decryptContent(CustomFile customFile) {
        try {
            return decryptContentFile.decrypt(customFile);
        } catch (Exception exception) {
            throw new AccessDeniedException();
        }
    }

    public CustomFile handleWithPassword(String fileName, String password) throws Exception {
        CustomFile customFile = getCustomFromFileSystem(fileName);
        return decryptContentFile.decryptWithPassword(customFile, password);
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
