package unit.domain.usecases;

import domain.entities.CustomFile;
import domain.exceptions.DuplicateFileNameException;
import domain.exceptions.EmptyFileException;
import domain.exceptions.MissingFileExsception;
import domain.ports.repository.FileMetadaRepository;
import domain.usecases.EncryptContentFile;
import domain.usecases.GenerateNewContent;
import domain.usecases.UploadFile;
import domain.values.CryptoParams;
import org.junit.Before;
import org.junit.Test;
import unit.adapters.gateway.InMemoryFileSystemGateway;
import unit.adapters.gateway.InMemorySecurityGateway;
import unit.adapters.repository.InMemoryFileMetadaRepository;

import static domain.values.STR.EMPTY;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static unit.domain.usecases.TestFileUtils.*;

public class UploadFileTest {

    private InMemoryFileSystemGateway memoryFileSystemGateway;
    private UploadFile uploadFile;

    @Before
    public void setUp(){
        memoryFileSystemGateway = new InMemoryFileSystemGateway();
        InMemorySecurityGateway memorySecurityGateway = new InMemorySecurityGateway();
        FileMetadaRepository fileMetadaRepository = new InMemoryFileMetadaRepository();
        GenerateNewContent generateNewContent = new GenerateNewContent(memorySecurityGateway);
        EncryptContentFile encryptContentFile = new EncryptContentFile(generateNewContent);
        uploadFile = new UploadFile(memoryFileSystemGateway, encryptContentFile, fileMetadaRepository);
    }

    @Test(expected = MissingFileExsception.class)
    public void upload_null_file_should_throw_missing_file_exception() throws Exception {
        //WHEN
        uploadFile.handle(null, CryptoParams.of(false, EMPTY));
    }

    @Test
    public void upload_file_should_save_it_in_directory() throws Exception {
        //GIVEN
        CustomFile customFileWithMonFichier = createcustomFileWithMonFichier(MON_FICHIER_TXT);

        //WHEN
        uploadFile.handle(customFileWithMonFichier, CryptoParams.of(false, EMPTY));

        //THEN
        CustomFile found = memoryFileSystemGateway.read(MON_FICHIER_TXT).orElseThrow();
        assertThat(getContentFileAsString(found), is(equalTo("Bonjour, tout le monde !!!")));
    }

    @Test(expected = DuplicateFileNameException.class)
    public void upload_existing_fileName_should_throw_duplicate_fileName() throws Exception {
        //GIVEN
        CustomFile customFileWithMonFichier = createcustomFileWithMonFichier(MON_FICHIER_TXT);

        //WHEN
        uploadFile.handle(customFileWithMonFichier, CryptoParams.of(false, EMPTY));
        uploadFile.handle(customFileWithMonFichier, CryptoParams.of(false, EMPTY));
    }

    @Test(expected = EmptyFileException.class)
    public void upload_empty_file_should_throw_empty_exception() throws Exception {
        //GIVEN
        CustomFile customFileWithMonFichier = createcustomFileWithMonFichier(EMPTY_FILE_TXT);

        //WHEN
        uploadFile.handle(customFileWithMonFichier, CryptoParams.of(false, EMPTY));
    }

    @Test
    public void upload_file_and_encrypt_its_content_should_save_encrypt_file_in_directory() throws Exception {
        //GIVEN
        CustomFile customFileWithMonFichier = createcustomFileWithMonFichier(MON_FICHIER_TXT);

        //WHEN
        uploadFile.handle(customFileWithMonFichier, CryptoParams.of(true, EMPTY));

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

}
