package fr.salim.equisign.adapters.repository;

import fr.salim.equisign.adapters.repository.entities.SerializableMetadata;
import fr.salim.equisign.domain.ports.repository.FileMetadataRepository;
import fr.salim.equisign.domain.entities.FileMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.Function.*;

@Repository
public class InFileFileMetadataRepository implements FileMetadataRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(InFileFileMetadataRepository.class);
    final String databasePath;
    final Map<String, SerializableMetadata> metadata;

    public InFileFileMetadataRepository(@Value("${databasePath}") String databasePath) {
        this.databasePath = databasePath;
        metadata = readAll().stream().collect(Collectors.toMap(m -> m.name, identity()));
    }

    public void save(FileMetadata fileMetadata) {
        try (FileOutputStream f = new FileOutputStream(databasePath)) {
            try(ObjectOutputStream o = new ObjectOutputStream(f)){
                SerializableMetadata meta = new SerializableMetadata(fileMetadata);
                o.writeObject(meta);
                metadata.put(meta.name, meta);
            }
        } catch (IOException e) {
            throw new RuntimeException("error while writing metadata in database", e);
        }
    }

    public Optional<FileMetadata> findByName(String fileName) {
        SerializableMetadata meta = metadata.get(fileName);
        return meta != null ? Optional.of(meta.toMetadata()) : Optional.empty();
    }

    public boolean exists(String name) {
        try (FileInputStream f = new FileInputStream(databasePath)) {
            return checkEachMeta(name, f);
        } catch (Exception e) {
            throw new RuntimeException("error while reading metadata in database", e);
        }
    }

    private List<SerializableMetadata> readAll() {
        List<SerializableMetadata> metadata = new ArrayList<>();
        try (FileInputStream f = new FileInputStream(databasePath)) {
            readEachMeta(metadata, f);
        } catch (Exception e) {
            throw new RuntimeException("error while reading metadata in database", e);
        }
        return metadata;
    }

    private void readEachMeta(List<SerializableMetadata> metadata, FileInputStream f) throws IOException, ClassNotFoundException {
        try(ObjectInputStream i = new ObjectInputStream(f)){
            for(;;) metadata.add((SerializableMetadata) i.readObject());
        } catch (EOFException endOfStream){
            if(LOGGER.isDebugEnabled()) LOGGER.debug("end of file!");
        }
    }

    private boolean checkEachMeta(String name, FileInputStream f) throws IOException, ClassNotFoundException {
        try(ObjectInputStream i = new ObjectInputStream(f)){
            for(;;){
                SerializableMetadata o = (SerializableMetadata) i.readObject();
                if(o.name.equals(name)) return true;
            }
        } catch (EOFException endOfStream){
            return false;
        }
    }

}
