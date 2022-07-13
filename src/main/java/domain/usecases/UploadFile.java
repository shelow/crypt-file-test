package domain.usecases;

import domain.entities.CustomFile;
import domain.exceptions.MissingFileExsception;
import domain.ports.gateway.FileSystemGateway;

public class UploadFile {

    private final FileSystemGateway fileSystemGateway;
    private EncryptContentFile encryptContentFile;

    public UploadFile(FileSystemGateway fileSystemGateway, EncryptContentFile encryptContentFile) {
        this.fileSystemGateway = fileSystemGateway;
        this.encryptContentFile = encryptContentFile;
    }

    public void handle(CustomFile file, boolean encryptFile) {
        checkFile(file);
        fileSystemGateway.write(generateFileToSave(file, encryptFile));
    }

    private CustomFile generateFileToSave(CustomFile file, boolean encryptFile) {
        return encryptFile ? encryptFile(file) : file;
    }

    private CustomFile encryptFile(CustomFile file) {
        try {
            return encryptContentFile.createNewEncryptContentFile(file);
        } catch (Exception e) {
            throw new RuntimeException("Error occured while encrypting the file "+file.name, e);
        }
    }



    private void checkFile(CustomFile file) {
        if(file == null) throw new MissingFileExsception();
    }
}
