package unit.adapters.gateway;

import domain.entities.CustomFile;
import domain.ports.gateway.FileSystemGateway;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryFileSystemGateway implements FileSystemGateway {

    Map<String, CustomFile> fileSaved = new HashMap<>();

    public boolean write(CustomFile file) {
        fileSaved.put(file.name, file);
        return true;
    }

    public Optional<CustomFile> read(String fileName) {
        return Optional.ofNullable(fileSaved.get(fileName));
    }
}
