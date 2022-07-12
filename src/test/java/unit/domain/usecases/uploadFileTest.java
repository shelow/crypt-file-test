package unit.domain.usecases;

import org.junit.Test;
import unit.domain.exceptions.MissingFileExsception;

import java.io.File;

public class uploadFileTest {

    @Test(expected = MissingFileExsception.class)
    public void upload_null_file_should_throw_missing_file_exception(){
        UploadFile uploadFile = new UploadFile();
        uploadFile.handle(null);
    }
}
