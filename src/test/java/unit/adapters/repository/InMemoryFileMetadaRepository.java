package unit.adapters.repository;

import domain.ports.repository.FileMetadaRepository;
import domain.values.FileMetadata;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryFileMetadaRepository implements FileMetadaRepository {

    private Map<String, FileMetadata> files = new HashMap<>();

    public void save(FileMetadata fileMetadata) {
        files.put(fileMetadata.name, new FileMetadata(fileMetadata));
    }

    public Optional<FileMetadata> findByName(String fileName) {
           return Optional.ofNullable(files.get(fileName));
    }

    public boolean exists(String name) {
        return files.containsKey(name);
    }

}
