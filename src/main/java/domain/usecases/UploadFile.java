package domain.usecases;

import domain.entities.CustomFile;
import domain.entities.CryptoParams;
import domain.exceptions.DuplicateFileNameException;
import domain.exceptions.EmptyFileException;
import domain.exceptions.MissingFileExsception;
import domain.ports.gateway.FileSystemGateway;
import domain.ports.repository.FileMetadaRepository;
import domain.values.FileMetadata;

public class UploadFile {

    private final FileSystemGateway fileSystemGateway;
    private final EncryptContentFile encryptContentFile;
    private final FileMetadaRepository fileMetadaRepository;

    public UploadFile(FileSystemGateway fileSystemGateway, EncryptContentFile encryptContentFile, FileMetadaRepository fileMetadaRepository) {
        this.fileSystemGateway = fileSystemGateway;
        this.encryptContentFile = encryptContentFile;
        this.fileMetadaRepository = fileMetadaRepository;
    }

    public void handle(CustomFile file, CryptoParams params) throws Exception {
        checkFile(file);
        CustomFile generatedFile = generateFileToSave(file, params);
        fileSystemGateway.write(generatedFile);
        fileMetadaRepository.save(new FileMetadata(generatedFile, params));
    }

    private CustomFile generateFileToSave(CustomFile file, CryptoParams params) throws Exception {
        return params.shouldEncryptFile ? encryptContentFile.encrypt(file, params) : file;
    }

    private void checkFile(CustomFile file) {
        if(file == null) throw new MissingFileExsception();
        if(file.content.length == 0) throw new EmptyFileException();
        if(fileMetadaRepository.exists(file.name)) throw new DuplicateFileNameException();
    }
}
