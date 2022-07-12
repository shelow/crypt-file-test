package unit.domain.usecases;

import domain.entities.CustomFile;
import domain.ports.directory.FileDirectory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class InMemoryFileDirectory implements FileDirectory {

    Map<String, CustomFile> fileSaved = new HashMap<>();

    @Override
    public boolean write(CustomFile file) {
        fileSaved.put(file.fileName, file);
        return true;
    }

    @Override
    public boolean exists(String fileName) {
        return fileSaved.containsKey(fileName);
    }
}
