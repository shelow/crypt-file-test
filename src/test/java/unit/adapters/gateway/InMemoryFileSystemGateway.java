package unit.adapters.gateway;

import fr.salim.equisign.domain.entities.CustomFile;
import fr.salim.equisign.domain.ports.gateway.FileSystemGateway;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryFileSystemGateway implements FileSystemGateway {

    Map<String, CustomFile> fileSaved = new HashMap<>();

    public void write(CustomFile file) {
        fileSaved.put(file.name, file);
    }

    public Optional<CustomFile> read(String fileName) {
        return Optional.ofNullable(fileSaved.get(fileName));
    }

    public void remove(String fileName) {
        fileSaved.remove(fileName);
    }
}
