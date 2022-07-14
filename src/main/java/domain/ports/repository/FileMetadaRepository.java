package domain.ports.repository;

import domain.values.FileMetadata;

import java.util.Optional;

public interface FileMetadaRepository {
    void save(FileMetadata fileMetadata);

    Optional<FileMetadata> findByName(String fileName);

    boolean exists(String name);
}
