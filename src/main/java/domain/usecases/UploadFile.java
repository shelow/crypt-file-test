package domain.usecases;

import domain.entities.CustomFile;
import domain.entities.UploadParams;
import domain.exceptions.EmptyFileException;
import domain.exceptions.MissingFileExsception;
import domain.ports.gateway.FileSystemGateway;

public class UploadFile {

    private final FileSystemGateway fileSystemGateway;
    private EncryptContentFile encryptContentFile;

    public UploadFile(FileSystemGateway fileSystemGateway, EncryptContentFile encryptContentFile) {
        this.fileSystemGateway = fileSystemGateway;
        this.encryptContentFile = encryptContentFile;
    }

    public void handle(CustomFile file, UploadParams params) {
        checkFile(file);
        fileSystemGateway.write(generateFileToSave(file, params));
    }

    private CustomFile generateFileToSave(CustomFile file, UploadParams params) {
        return params.shouldEncryptFile ? encryptFile(file, params) : file;
    }

    private CustomFile encryptFile(CustomFile file, UploadParams params) {
        try {
            return encryptContentFile.createNewEncryptContentFile(file, params);
        } catch (Exception e) {
            throw new RuntimeException("Error occured while encrypting the file "+file.name, e);
        }
    }

    private void checkFile(CustomFile file) {
        if(file == null) throw new MissingFileExsception();
        if(file.content.length == 0) throw new EmptyFileException();
    }
}
