package domain.usecases;

import domain.exceptions.NotFoundException;
import domain.ports.gateway.FileSystemGateway;

public class DownloadFile {
    private FileSystemGateway fileSystemGateway;

    public DownloadFile(FileSystemGateway fileSystemGateway) {
        this.fileSystemGateway = fileSystemGateway;
    }

    public void handle(String fileName) {
        throw new NotFoundException("Le fichier "+fileName+" n'existe pas");
    }
}
