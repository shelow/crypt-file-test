package unit.domain.usecases;

import domain.entities.CustomFile;
import domain.entities.UploadParams;
import domain.exceptions.NotFoundException;
import domain.usecases.DownloadFile;
import domain.usecases.EncryptContentFile;
import domain.usecases.UploadFile;
import org.junit.Before;
import org.junit.Test;
import unit.adapters.InMemoryFileSystemGateway;
import unit.adapters.InMemorySecurityGateway;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static unit.domain.usecases.TestFileUtils.MON_FICHIER_TXT;
import static unit.domain.usecases.TestFileUtils.createcustomFileWithMonFichier;

public class downloadFileTest {

    private InMemoryFileSystemGateway memoryFileSystemGateway;
    private InMemorySecurityGateway memorySecurityGateway;
    private UploadFile uploadFile;
    private DownloadFile downloadFile;

    @Before
    public void setUp(){
        memoryFileSystemGateway = new InMemoryFileSystemGateway();
        memorySecurityGateway = new InMemorySecurityGateway();
        EncryptContentFile encryptContentFile = new EncryptContentFile(memorySecurityGateway);
        uploadFile = new UploadFile(memoryFileSystemGateway, encryptContentFile);
        downloadFile = new DownloadFile(memoryFileSystemGateway);
    }

    @Test(expected = NotFoundException.class)
    public void download_unknown_file_should_throw_not_found(){
        downloadFile.handle("unknown_file.txt");
    }

    @Test
    public void download_file_should_return_requested_file(){
        //GIVEN
        CustomFile customFile = createcustomFileWithMonFichier(MON_FICHIER_TXT);
        UploadParams params = UploadParams.of(false);
        uploadFile.handle(customFile, params);

        //WHEN
        CustomFile file = downloadFile.handle(MON_FICHIER_TXT);

        //THEN
        assertThat(file, is(equalTo(customFile)));
    }

    @Test
    public void download_file_encrypt_should_return_requested_file(){
        //GIVEN
        CustomFile sourceFile = createcustomFileWithMonFichier(MON_FICHIER_TXT);
        UploadParams params = UploadParams.of(true);
        uploadFile.handle(sourceFile, params);

        //WHEN
        CustomFile foundFile = downloadFile.handle(MON_FICHIER_TXT);

        //THEN
        assertThat(foundFile + " != " + sourceFile,foundFile, is(equalTo(sourceFile)));
    }
}
