package unit.domain.usecases;

import unit.domain.exceptions.MissingFileExsception;

import java.io.File;

public class UploadFile {
    public void handle(File file) {
        throw new MissingFileExsception();
    }
}
