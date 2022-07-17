package fr.salim.equisign.domain.usecases;

import fr.salim.equisign.domain.entities.CustomFile;
import fr.salim.equisign.domain.values.CryptoParams;
import fr.salim.equisign.domain.exceptions.DuplicateFileNameException;
import fr.salim.equisign.domain.exceptions.EmptyFileException;
import fr.salim.equisign.domain.exceptions.MissingFileException;
import fr.salim.equisign.domain.ports.gateway.FileSystemGateway;
import fr.salim.equisign.domain.ports.repository.FileMetadataRepository;
import fr.salim.equisign.domain.entities.FileMetadata;

public class UploadFile {

    private final FileSystemGateway fileSystemGateway;
    private final EncryptContentFile encryptContentFile;
    private final FileMetadataRepository fileMetadataRepository;

    public UploadFile(FileSystemGateway fileSystemGateway, EncryptContentFile encryptContentFile, FileMetadataRepository fileMetadataRepository) {
        this.fileSystemGateway = fileSystemGateway;
        this.encryptContentFile = encryptContentFile;
        this.fileMetadataRepository = fileMetadataRepository;
    }

    public void handle(CustomFile file, CryptoParams params) throws Exception {
        checkFile(file);
        CustomFile generatedFile = generateFileToSave(file, params);
        fileSystemGateway.write(generatedFile);
        fileMetadataRepository.save(new FileMetadata(generatedFile, params));
    }

    private CustomFile generateFileToSave(CustomFile file, CryptoParams params) throws Exception {
        return params.shouldEncryptFile ? encryptContentFile.encrypt(file, params) : file;
    }

    private void checkFile(CustomFile file) {
        if(file == null) throw new MissingFileException();
        if(file.content.length == 0) throw new EmptyFileException();
        if(fileMetadataRepository.exists(file.name)) throw new DuplicateFileNameException("Le fichier "+file.name+" existe déja sur le serveur");
    }
}
