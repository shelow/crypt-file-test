package fr.salim.equisign.adapters.repository.entities;

import fr.salim.equisign.domain.entities.FileMetadata;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SerializableMetadata implements Serializable {
    public final String name;
    public final boolean encrypted;
    public final boolean withPassword;
    public final LocalDateTime saveDate;
    public SerializableMetadata(FileMetadata fileMetadata) {
        this.name = fileMetadata.name;
        this.encrypted = fileMetadata.encrypted;
        this.withPassword = fileMetadata.withPassword;
        this.saveDate = fileMetadata.saveDate;
    }

    public FileMetadata toMetadata() {
        return new FileMetadata(name, encrypted, withPassword, saveDate);
    }
}
