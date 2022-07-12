package domain.entities;

import java.io.FileInputStream;

public class CustomFile {
    public final String fileName;
    public final FileInputStream file;

    public CustomFile(String fileName, FileInputStream file) {
        this.fileName = fileName;
        this.file = file;
    }
}
