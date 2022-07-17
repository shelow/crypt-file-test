package unit.adapters.repository;

import fr.salim.equisign.domain.ports.repository.FileMetadataRepository;
import fr.salim.equisign.domain.entities.FileMetadata;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryFileMetadataRepository implements FileMetadataRepository {

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
