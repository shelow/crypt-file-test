package unit.domain.usecases;

import domain.entities.CustomFile;
import domain.entities.UploadParams;
import domain.exceptions.MissingFileExsception;
import domain.usecases.EncryptContentFile;
import domain.usecases.UploadFile;
import org.junit.Before;
import org.junit.Test;
import unit.adapters.InMemoryFileSystemGateway;
import unit.adapters.InMemorySecurityGateway;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class uploadFileTest {

    public static final String MON_FICHIER_TXT = "mon_fichier.txt";
    public static final String POINT = ".";
    public static final String SRC = "src";
    public static final String TEST = "test";
    public static final String RESOURCES = "resources";
    private InMemoryFileSystemGateway memoryFileSystemGateway;
    private InMemorySecurityGateway memorySecurityGateway;
    private UploadFile uploadFile;

    @Before
    public void setUp(){
        memoryFileSystemGateway = new InMemoryFileSystemGateway();
        memorySecurityGateway = new InMemorySecurityGateway();
        EncryptContentFile encryptContentFile = new EncryptContentFile(memorySecurityGateway);
        uploadFile = new UploadFile(memoryFileSystemGateway, encryptContentFile);
    }

    @Test(expected = MissingFileExsception.class)
    public void upload_null_file_should_throw_missing_file_exception(){
        //WHEN
        uploadFile.handle(null, UploadParams.of(false));
    }

    @Test
    public void upload_file_should_save_it_in_directory() throws FileNotFoundException {
        //GIVEN
        CustomFile custumeFileWithMonFichier = createCustumeFileWithMonFichier();

        //WHEN
        uploadFile.handle(custumeFileWithMonFichier, UploadParams.of(false));

        //THEN
        CustomFile found = memoryFileSystemGateway.read("mon_fichier.txt").orElseThrow();
        assertThat(getContentFileAsString(found), is(equalTo("Bonjour, tout le monde !!!")));
    }

    @Test
    public void upload_file_and_encrypt_its_content_should_save_encrypt_file_in_directory() throws FileNotFoundException {
        //GIVEN
        CustomFile custumeFileWithMonFichier = createCustumeFileWithMonFichier();

        //WHEN
        uploadFile.handle(custumeFileWithMonFichier, UploadParams.of(true));

        //THEN
        CustomFile found = memoryFileSystemGateway.read("mon_fichier.txt").orElseThrow();
        assertThat(getContentFileAsString(found), is(not(equalTo("Bonjour, tout le monde !!!"))));
    }

    @Test
    public void upload_file_and_encrypt_with_password_should_save_encrypt_file_in_directory() throws FileNotFoundException {
        //GIVEN
        CustomFile custumeFileWithMonFichier = createCustumeFileWithMonFichier();

        //WHEN
        UploadParams uploadParams = UploadParams.of(true, "password");
        uploadFile.handle(custumeFileWithMonFichier, uploadParams);

        //THEN
        CustomFile found = memoryFileSystemGateway.read("mon_fichier.txt").orElseThrow();
        assertThat(getContentFileAsString(found), is(not(equalTo("Bonjour, tout le monde !!!"))));
    }

    private String getContentFileAsString(CustomFile found) {
        return new String(found.content, StandardCharsets.UTF_8);
    }

    private CustomFile createCustumeFileWithMonFichier() {
        try {
            return new CustomFile(MON_FICHIER_TXT, Files.readAllBytes(Paths.get(POINT, SRC, TEST, RESOURCES, MON_FICHIER_TXT)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
