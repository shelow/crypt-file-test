package unit.domain.usecases;

import domain.entities.CustomFile;
import domain.exceptions.MissingFileExsception;
import domain.ports.directory.FileDirectory;

public class UploadFile {
    private final FileDirectory fileDirectory;

    public UploadFile(FileDirectory fileDirectory) {
        this.fileDirectory = fileDirectory;
    }

    public void handle(CustomFile file) {
        if(file == null) throw new MissingFileExsception();
        fileDirectory.write(file);
    }
}
