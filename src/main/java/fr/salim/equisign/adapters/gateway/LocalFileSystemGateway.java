package fr.salim.equisign.adapters.gateway;

import fr.salim.equisign.domain.entities.CustomFile;
import fr.salim.equisign.domain.ports.gateway.FileSystemGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Component
public class LocalFileSystemGateway implements FileSystemGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalFileSystemGateway.class);
    final String destinationDir;

    public LocalFileSystemGateway(@Value("${destinationDirectory}") String destinationDir) {
        this.destinationDir = destinationDir;
    }

    public void write(CustomFile file) {
        try{
            LOGGER.info("Ecriture du fichier "+file.name+" sur le disque");
            Files.write(Path.of(destinationDir + file.name), file.content);
        } catch (Exception e){
            throw new RuntimeException("error while writing file " + file.name);
        }
    }

    public Optional<CustomFile> read(String fileName) {
        try {
            if(!Files.exists(Paths.get(destinationDir + fileName))) return Optional.empty();
            byte[] bytes = Files.readAllBytes(Paths.get(destinationDir + fileName));
            return Optional.of(new CustomFile(fileName, bytes));
        } catch (IOException e) {
            throw new RuntimeException("error while reading the file " + fileName);
        }
    }
}
