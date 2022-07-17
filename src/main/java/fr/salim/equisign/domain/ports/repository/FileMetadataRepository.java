package fr.salim.equisign.domain.ports.repository;

import fr.salim.equisign.domain.entities.FileMetadata;

import java.util.Optional;

public interface FileMetadataRepository {
    void save(FileMetadata fileMetadata);

    Optional<FileMetadata> findByName(String fileName);

    boolean exists(String name);
}
