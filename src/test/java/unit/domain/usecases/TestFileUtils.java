package unit.domain.usecases;

import domain.entities.CustomFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestFileUtils {

    public static final String MON_FICHIER_TXT = "mon_fichier.txt";
    public static final String POINT = ".";
    public static final String SRC = "src";
    public static final String TEST = "test";
    public static final String RESOURCES = "resources";
    public static final String EMPTY_FILE_TXT = "empty_file.txt";

    public static CustomFile createcustomFileWithMonFichier(String fileName) {
        try {
            return new CustomFile(fileName, Files.readAllBytes(Paths.get(POINT, SRC, TEST, RESOURCES, fileName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getContentFileAsString(CustomFile found) {
        String res = new String(found.content, StandardCharsets.UTF_8);
        if(res.isEmpty()) throw new NullPointerException();
        return res;
    }
}
