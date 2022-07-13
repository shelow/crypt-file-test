package domain.ports.gateway;

import domain.entities.CustomFile;

import java.util.Optional;

public interface FileSystemGateway {

    boolean write(CustomFile file);

    Optional<CustomFile> read(String String);
}
