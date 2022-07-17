package fr.salim.equisign.domain.entities;

import fr.salim.equisign.domain.values.CryptoParams;

import java.time.LocalDateTime;
import java.util.Objects;

public class FileMetadata {

    public String name;
    public boolean encrypted;
    public boolean withPassword;
    public LocalDateTime saveDate;

    public FileMetadata(String name, boolean encrypted, boolean withPassword, LocalDateTime saveDate) {
        this.name = name;
        this.encrypted = encrypted;
        this.withPassword = withPassword;
        this.saveDate = saveDate;
    }

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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMetadata that = (FileMetadata) o;
        return encrypted == that.encrypted && withPassword == that.withPassword && Objects.equals(name, that.name) && Objects.equals(saveDate, that.saveDate);
    }
}