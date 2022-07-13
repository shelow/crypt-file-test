package unit.domain.usecases;

import domain.exceptions.NotFoundException;
import domain.usecases.DownloadFile;
import domain.usecases.EncryptContentFile;
import domain.usecases.UploadFile;
import org.junit.Before;
import org.junit.Test;
import unit.adapters.InMemoryFileSystemGateway;
import unit.adapters.InMemorySecurityGateway;

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
}
