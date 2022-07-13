package domain.usecases;

import domain.entities.CustomFile;
import domain.exceptions.NotFoundException;
import domain.ports.gateway.FileSystemGateway;

public class DownloadFile {
    private FileSystemGateway fileSystemGateway;

    public DownloadFile(FileSystemGateway fileSystemGateway) {
        this.fileSystemGateway = fileSystemGateway;
    }

    public CustomFile handle(String fileName) {
        return fileSystemGateway.read(fileName)
                .orElseThrow( () -> new NotFoundException("Le fichier "+fileName+" n'existe pas"));
    }
}
