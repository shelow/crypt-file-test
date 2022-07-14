package unit.domain.usecases;

import domain.entities.CustomFile;
import domain.values.CryptoParams;
import domain.exceptions.DuplicateFileNameException;
import domain.exceptions.EmptyFileException;
import domain.exceptions.MissingFileExsception;
import domain.exceptions.MissingPasswordException;
import domain.ports.repository.FileMetadaRepository;
import domain.usecases.EncryptContentFile;
import domain.usecases.GenerateNewContent;
import domain.usecases.UploadFile;
import org.junit.Before;
import org.junit.Test;
import unit.adapters.gateway.InMemoryFileSystemGateway;
import unit.adapters.gateway.InMemorySecurityGateway;
import unit.adapters.repository.InMemoryFileMetadaRepository;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static unit.domain.usecases.TestFileUtils.*;

public class UploadFileTest {

    private InMemoryFileSystemGateway memoryFileSystemGateway;
    private InMemorySecurityGateway memorySecurityGateway;
    private UploadFile uploadFile;
    private FileMetadaRepository fileMetadaRepository;

    @Before
    public void setUp(){
        memoryFileSystemGateway = new InMemoryFileSystemGateway();
        memorySecurityGateway = new InMemorySecurityGateway();
        fileMetadaRepository = new InMemoryFileMetadaRepository();
        GenerateNewContent generateNewContent = new GenerateNewContent(memorySecurityGateway);
        EncryptContentFile encryptContentFile = new EncryptContentFile(generateNewContent);
        uploadFile = new UploadFile(memoryFileSystemGateway, encryptContentFile, fileMetadaRepository);
    }

    @Test(expected = MissingFileExsception.class)
    public void upload_null_file_should_throw_missing_file_exception() throws Exception {
        //WHEN
        uploadFile.handle(null, CryptoParams.of(false));
    }

    @Test
    public void upload_file_should_save_it_in_directory() throws Exception {
        //GIVEN
        CustomFile customFileWithMonFichier = createcustomFileWithMonFichier(MON_FICHIER_TXT);

        //WHEN
        uploadFile.handle(customFileWithMonFichier, CryptoParams.of(false));

        //THEN
        CustomFile found = memoryFileSystemGateway.read(MON_FICHIER_TXT).orElseThrow();
        assertThat(getContentFileAsString(found), is(equalTo("Bonjour, tout le monde !!!")));
    }

    @Test(expected = DuplicateFileNameException.class)
    public void upload_existing_fileName_should_throw_duplicate_fileName() throws Exception {
        //GIVEN
        CustomFile customFileWithMonFichier = createcustomFileWithMonFichier(MON_FICHIER_TXT);

        //WHEN
        uploadFile.handle(customFileWithMonFichier, CryptoParams.of(false));
        uploadFile.handle(customFileWithMonFichier, CryptoParams.of(false));
    }

    @Test(expected = EmptyFileException.class)
    public void upload_empty_file_should_throw_empty_exception() throws Exception {
        //GIVEN
        CustomFile customFileWithMonFichier = createcustomFileWithMonFichier(EMPTY_FILE_TXT);

        //WHEN
        uploadFile.handle(customFileWithMonFichier, CryptoParams.of(false));
    }

    @Test
    public void upload_file_and_encrypt_its_content_should_save_encrypt_file_in_directory() throws Exception {
        //GIVEN
        CustomFile customFileWithMonFichier = createcustomFileWithMonFichier(MON_FICHIER_TXT);

        //WHEN
        uploadFile.handle(customFileWithMonFichier, CryptoParams.of(true));

        //THEN
        CustomFile found = memoryFileSystemGateway.read(MON_FICHIER_TXT).orElseThrow();
        assertThat(getContentFileAsString(found), is(not(equalTo("Bonjour, tout le monde !!!"))));
    }

    @Test
    public void upload_file_and_encrypt_with_password_should_save_encrypt_file_in_directory() throws Exception {
        //GIVEN
        CustomFile customFileWithMonFichier = createcustomFileWithMonFichier(MON_FICHIER_TXT);

        //WHEN
        CryptoParams cryptoParams = CryptoParams.of(true, "password");
        uploadFile.handle(customFileWithMonFichier, cryptoParams);

        //THEN
        CustomFile found = memoryFileSystemGateway.read(MON_FICHIER_TXT).orElseThrow();
        assertThat(getContentFileAsString(found), is(not(equalTo("Bonjour, tout le monde !!!"))));
    }

    @Test(expected = MissingPasswordException.class)
    public void upload_file_with_empty_password_should_throw_password_missing_exception() {
        //WHEN
        CryptoParams.of(true, "");
    }

    @Test(expected = MissingPasswordException.class)
    public void upload_file_with_null_password_should_throw_password_missing_exception() {
       //WHEN
        CryptoParams.of(true, null);
    }

}
