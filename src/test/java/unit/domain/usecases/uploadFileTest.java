package unit.domain.usecases;

import domain.entities.CustomFile;
import domain.entities.UploadParams;
import domain.exceptions.EmptyFileException;
import domain.exceptions.MissingFileExsception;
import domain.exceptions.MissingPasswordException;
import domain.usecases.EncryptContentFile;
import domain.usecases.UploadFile;
import org.junit.Before;
import org.junit.Test;
import unit.adapters.InMemoryFileSystemGateway;
import unit.adapters.InMemorySecurityGateway;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static unit.domain.usecases.TestFileUtils.*;

public class uploadFileTest {

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
    public void upload_file_should_save_it_in_directory() {
        //GIVEN
        CustomFile customFileWithMonFichier = createcustomFileWithMonFichier(MON_FICHIER_TXT);

        //WHEN
        uploadFile.handle(customFileWithMonFichier, UploadParams.of(false));

        //THEN
        CustomFile found = memoryFileSystemGateway.read(MON_FICHIER_TXT).orElseThrow();
        assertThat(getContentFileAsString(found), is(equalTo("Bonjour, tout le monde !!!")));
    }

    @Test(expected = EmptyFileException.class)
    public void upload_empty_file_should_throw_empty_exception() {
        //GIVEN
        CustomFile customFileWithMonFichier = createcustomFileWithMonFichier(EMPTY_FILE_TXT);

        //WHEN
        uploadFile.handle(customFileWithMonFichier, UploadParams.of(false));
    }

    @Test
    public void upload_file_and_encrypt_its_content_should_save_encrypt_file_in_directory() {
        //GIVEN
        CustomFile customFileWithMonFichier = createcustomFileWithMonFichier(MON_FICHIER_TXT);

        //WHEN
        uploadFile.handle(customFileWithMonFichier, UploadParams.of(true));

        //THEN
        CustomFile found = memoryFileSystemGateway.read(MON_FICHIER_TXT).orElseThrow();
        assertThat(getContentFileAsString(found), is(not(equalTo("Bonjour, tout le monde !!!"))));
    }

    @Test
    public void upload_file_and_encrypt_with_password_should_save_encrypt_file_in_directory() {
        //GIVEN
        CustomFile customFileWithMonFichier = createcustomFileWithMonFichier(MON_FICHIER_TXT);

        //WHEN
        UploadParams uploadParams = UploadParams.of(true, "password");
        uploadFile.handle(customFileWithMonFichier, uploadParams);

        //THEN
        CustomFile found = memoryFileSystemGateway.read(MON_FICHIER_TXT).orElseThrow();
        assertThat(getContentFileAsString(found), is(not(equalTo("Bonjour, tout le monde !!!"))));
    }

    @Test(expected = MissingPasswordException.class)
    public void upload_file_with_empty_password_should_throw_password_missing_exception() {
        //WHEN
        UploadParams.of(true, "");
    }

    @Test(expected = MissingPasswordException.class)
    public void upload_file_with_null_password_should_throw_password_missing_exception() {
       //WHEN
        UploadParams.of(true, null);
    }

    private String getContentFileAsString(CustomFile found) {
        String res = new String(found.content, StandardCharsets.UTF_8);
        if(res.isEmpty()) throw new NullPointerException();
        return res;
    }

}
