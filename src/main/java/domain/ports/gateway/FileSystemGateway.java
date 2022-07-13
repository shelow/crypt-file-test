package domain.ports.gateway;

import domain.entities.CustomFile;

import java.util.Optional;

public interface FileSystemGateway {

    boolean write(CustomFile file);

    boolean exists(String fileName);

    Optional<CustomFile> read(String String);
}
