package domain.values;

import domain.entities.CustomFile;

import java.time.LocalDateTime;

public class FileMetadata {
    public final String name;
    public final boolean encrypted;
    public final boolean withPassword;
    public final LocalDateTime saveDate;

    public FileMetadata(CustomFile file, CryptoParams params) {
        this.name = file.name;
        this.encrypted = params.shouldEncryptFile;
        this.withPassword = params.password.length > 0;
        this.saveDate = LocalDateTime.now();
    }

    public FileMetadata(FileMetadata fileMetadata) {
        this.name = fileMetadata.name;
        this.encrypted = fileMetadata.encrypted;
        this.withPassword = fileMetadata.withPassword;
        this.saveDate = fileMetadata.saveDate;
    }
}