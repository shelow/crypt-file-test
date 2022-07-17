package unit.domain.usecases;

import fr.salim.equisign.domain.entities.CustomFile;
import fr.salim.equisign.domain.exceptions.DuplicateFileNameException;
import fr.salim.equisign.domain.exceptions.EmptyFileException;
import fr.salim.equisign.domain.exceptions.MissingFileException;
import fr.salim.equisign.domain.ports.repository.FileMetadataRepository;
import fr.salim.equisign.domain.usecases.EncryptContentFile;
import fr.salim.equisign.domain.usecases.GenerateNewContent;
import fr.salim.equisign.domain.usecases.UploadFile;
import fr.salim.equisign.domain.values.CryptoParams;
import fr.salim.equisign.domain.entities.FileMetadata;
import org.junit.Before;
import org.junit.Test;
import unit.adapters.gateway.InMemoryFileSystemGateway;
import unit.adapters.gateway.InMemorySecurityGateway;
import unit.adapters.repository.InMemoryFileMetadataRepository;

import static fr.salim.equisign.domain.values.STR.EMPTY;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static unit.domain.usecases.TestFileUtils.*;

public class UploadFileTest {

    private InMemoryFileSystemGateway memoryFileSystemGateway;
    private UploadFile uploadFile;
    private FileMetadataRepository fileMetadataRepository;

    @Before
    public void setUp(){
        memoryFileSystemGateway = new InMemoryFileSystemGateway();
        InMemorySecurityGateway memorySecurityGateway = new InMemorySecurityGateway();
        fileMetadataRepository = new InMemoryFileMetadataRepository();
        GenerateNewContent generateNewContent = new GenerateNewContent(memorySecurityGateway);
        EncryptContentFile encryptContentFile = new EncryptContentFile(generateNewContent);
        uploadFile = new UploadFile(memoryFileSystemGateway, encryptContentFile, fileMetadataRepository);
    }

    @Test(expected = MissingFileException.class)
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
        FileMetadata saved = fileMetadataRepository.findByName(MON_FICHIER_TXT).orElseThrow();
        FileMetadata expected = new FileMetadata(MON_FICHIER_TXT, true, false, saved.saveDate);
        assertThat(saved, is(equalTo(expected)));
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
        FileMetadata saved = fileMetadataRepository.findByName(MON_FICHIER_TXT).orElseThrow();
        FileMetadata expected = new FileMetadata(MON_FICHIER_TXT, true, true, saved.saveDate);
        assertThat(saved, is(equalTo(expected)));
    }

}
