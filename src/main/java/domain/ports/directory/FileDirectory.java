package domain.ports.directory;

import domain.entities.CustomFile;

public interface FileDirectory {

    boolean write(CustomFile file);

    boolean exists(String fileName);
}
