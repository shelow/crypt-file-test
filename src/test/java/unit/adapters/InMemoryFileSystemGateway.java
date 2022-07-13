package unit.adapters;

import domain.entities.CustomFile;
import domain.ports.gateway.FileSystemGateway;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryFileSystemGateway implements FileSystemGateway {

    Map<String, CustomFile> fileSaved = new HashMap<>();

    @Override
    public boolean write(CustomFile file) {
        fileSaved.put(file.name, file);
        return true;
    }

    @Override
    public boolean exists(String fileName) {
        return fileSaved.containsKey(fileName);
    }

    @Override
    public Optional<CustomFile> read(String fileName) {
        return Optional.ofNullable(fileSaved.get(fileName));
    }
}
