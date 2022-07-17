package fr.salim.equisign.domain.ports.gateway;

import fr.salim.equisign.domain.entities.CustomFile;

import java.util.Optional;

public interface FileSystemGateway {

    void write(CustomFile file);

    Optional<CustomFile> read(String String);
}
