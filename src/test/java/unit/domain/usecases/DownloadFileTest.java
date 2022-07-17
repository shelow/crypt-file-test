package unit.domain.usecases;

import fr.salim.equisign.domain.entities.CustomFile;
import fr.salim.equisign.domain.exceptions.AccessDeniedException;
import fr.salim.equisign.domain.exceptions.NotFoundException;
import fr.salim.equisign.domain.ports.repository.FileMetadataRepository;
import fr.salim.equisign.domain.usecases.*;
import fr.salim.equisign.domain.values.CryptoParams;
import org.junit.Before;
import org.junit.Test;
import unit.adapters.gateway.InMemoryFileSystemGateway;
import unit.adapters.gateway.InMemorySecurityGateway;
import unit.adapters.repository.InMemoryFileMetadataRepository;

import static fr.salim.equisign.domain.values.STR.EMPTY;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static unit.domain.usecases.TestFileUtils.MON_FICHIER_TXT;
import static unit.domain.usecases.TestFileUtils.createcustomFileWithMonFichier;

public class DownloadFileTest {

    private UploadFile uploadFile;
    private DownloadFile downloadFile;
    private InMemoryFileSystemGateway memoryFileSystemGateway;

    @Before
    public void setUp(){
        memoryFileSystemGateway = new InMemoryFileSystemGateway();
        InMemorySecurityGateway memorySecurityGateway = new InMemorySecurityGateway();
        GenerateNewContent generateSecreteKey = new GenerateNewContent(memorySecurityGateway);
        EncryptContentFile encryptContentFile = new EncryptContentFile(generateSecreteKey);
        DecryptContentFile decryptContentFile = new DecryptContentFile(generateSecreteKey);
        FileMetadataRepository fileMetadataRepository = new InMemoryFileMetadataRepository();
        uploadFile = new UploadFile(memoryFileSystemGateway, encryptContentFile, fileMetadataRepository);
        downloadFile = new DownloadFile(memoryFileSystemGateway, fileMetadataRepository, decryptContentFile);
    }

    @Test(expected = NotFoundException.class)
    public void download_unknown_file_should_throw_not_found() {
        downloadFile.handle("unknown_file.txt", EMPTY);
    }

    @Test
    public void download_file_should_return_requested_file() throws Exception {
        //GIVEN
        CustomFile customFile = createcustomFileWithMonFichier(MON_FICHIER_TXT);
        CryptoParams params = CryptoParams.of(false, EMPTY);
        uploadFile.handle(customFile, params);

        //WHEN
        CustomFile file = downloadFile.handle(MON_FICHIER_TXT, EMPTY);

        //THEN
        assertThat(file, is(equalTo(customFile)));
    }

    @Test
    public void download_file_null_password_should_return_requested_file() throws Exception {
        //GIVEN
        CustomFile customFile = createcustomFileWithMonFichier(MON_FICHIER_TXT);
        CryptoParams params = CryptoParams.of(false, EMPTY);
        uploadFile.handle(customFile, params);

        //WHEN
        CustomFile file = downloadFile.handle(MON_FICHIER_TXT, null);

        //THEN
        assertThat(file, is(equalTo(customFile)));
    }

    @Test
    public void download_file_encrypted_should_return_decrypted_file() throws Exception {
        //GIVEN
        CustomFile sourceFile = createcustomFileWithMonFichier(MON_FICHIER_TXT);
        CryptoParams params = CryptoParams.of(true, EMPTY);
        uploadFile.handle(sourceFile, params);

        //WHEN
        CustomFile foundFile = downloadFile.handle(MON_FICHIER_TXT, EMPTY);

        //THEN
        assertThat(foundFile + " != " + sourceFile,foundFile, is(equalTo(sourceFile)));
    }

    @Test
    public void download_file_encrypted_with_password_should_return_decrypted_file() throws Exception {
        //GIVEN
        CustomFile sourceFile = createcustomFileWithMonFichier(MON_FICHIER_TXT);
        CryptoParams params = CryptoParams.of(true, "password");
        uploadFile.handle(sourceFile, params);

        //WHEN
        CustomFile foundFile = downloadFile.handle(MON_FICHIER_TXT, "password");

        //THEN
        assertThat(foundFile + " != " + sourceFile,foundFile, is(equalTo(sourceFile)));
    }

    @Test(expected = AccessDeniedException.class)
    public void download_file_encrypted_with_password_missing_password_in_param_should_throw_access_denied_exception() throws Exception {
        //GIVEN
        CustomFile sourceFile = createcustomFileWithMonFichier(MON_FICHIER_TXT);
        CryptoParams params = CryptoParams.of(true, "password");
        uploadFile.handle(sourceFile, params);

        //WHEN
        downloadFile.handle(MON_FICHIER_TXT, EMPTY);
    }

    @Test(expected = AccessDeniedException.class)
    public void download_file_encrypted_with_password_wrong_password_in_param_should_throw_access_denied_exception() throws Exception {
        //GIVEN
        CustomFile sourceFile = createcustomFileWithMonFichier(MON_FICHIER_TXT);
        CryptoParams params = CryptoParams.of(true, "wrong_password");
        uploadFile.handle(sourceFile, params);

        //WHEN
        downloadFile.handle(MON_FICHIER_TXT, EMPTY);
    }

    @Test(expected = AccessDeniedException.class)
    public void download_file_encrypted_without_but_with_password_in_param_should_throw_access_denied_exception() throws Exception {
        //GIVEN
        CustomFile sourceFile = createcustomFileWithMonFichier(MON_FICHIER_TXT);
        CryptoParams params = CryptoParams.of(true, EMPTY);
        uploadFile.handle(sourceFile, params);

        //WHEN
        downloadFile.handle(MON_FICHIER_TXT, "password");
    }

    @Test(expected = NotFoundException.class)
    public void download_file_encrypted_deleted_on_disk_should_throw_not_found() throws Exception {
        //GIVEN
        CustomFile sourceFile = createcustomFileWithMonFichier(MON_FICHIER_TXT);
        CryptoParams params = CryptoParams.of(true, EMPTY);
        uploadFile.handle(sourceFile, params);
        memoryFileSystemGateway.remove(MON_FICHIER_TXT);

        //WHEN
        downloadFile.handle(MON_FICHIER_TXT, EMPTY);
    }
}
