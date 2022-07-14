package unit.domain.usecases;

import domain.entities.CustomFile;
import domain.values.CryptoParams;
import domain.exceptions.NotFoundException;
import domain.ports.repository.FileMetadaRepository;
import domain.usecases.*;
import org.junit.Before;
import org.junit.Test;
import unit.adapters.repository.InMemoryFileMetadaRepository;
import unit.adapters.gateway.InMemoryFileSystemGateway;
import unit.adapters.gateway.InMemorySecurityGateway;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static unit.domain.usecases.TestFileUtils.MON_FICHIER_TXT;
import static unit.domain.usecases.TestFileUtils.createcustomFileWithMonFichier;

public class DownloadFileTest {

    private InMemoryFileSystemGateway memoryFileSystemGateway;
    private UploadFile uploadFile;
    private DownloadFile downloadFile;

    @Before
    public void setUp(){
        memoryFileSystemGateway = new InMemoryFileSystemGateway();
        InMemorySecurityGateway memorySecurityGateway = new InMemorySecurityGateway();
        GenerateNewContent generateSecreteKey = new GenerateNewContent(memorySecurityGateway);
        EncryptContentFile encryptContentFile = new EncryptContentFile(generateSecreteKey);
        DecryptContentFile decryptContentFile = new DecryptContentFile(generateSecreteKey);
        FileMetadaRepository fileMetadaRepository = new InMemoryFileMetadaRepository();
        uploadFile = new UploadFile(memoryFileSystemGateway, encryptContentFile, fileMetadaRepository);
        downloadFile = new DownloadFile(memoryFileSystemGateway, fileMetadaRepository, decryptContentFile);
    }

    @Test(expected = NotFoundException.class)
    public void download_unknown_file_should_throw_not_found() throws Exception {
        downloadFile.handle("unknown_file.txt");
    }

    @Test
    public void download_file_should_return_requested_file() throws Exception {
        //GIVEN
        CustomFile customFile = createcustomFileWithMonFichier(MON_FICHIER_TXT);
        CryptoParams params = CryptoParams.of(false);
        uploadFile.handle(customFile, params);

        //WHEN
        CustomFile file = downloadFile.handle(MON_FICHIER_TXT);

        //THEN
        assertThat(file, is(equalTo(customFile)));
    }

    @Test
    public void download_file_encrypted_should_return_decrypted_file() throws Exception {
        //GIVEN
        CustomFile sourceFile = createcustomFileWithMonFichier(MON_FICHIER_TXT);
        CryptoParams params = CryptoParams.of(true);
        uploadFile.handle(sourceFile, params);

        //WHEN
        CustomFile foundFile = downloadFile.handle(MON_FICHIER_TXT);

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
        CustomFile foundFile = downloadFile.handleWithPassword(MON_FICHIER_TXT, "password");

        //THEN
        assertThat(foundFile + " != " + sourceFile,foundFile, is(equalTo(sourceFile)));
    }
}
