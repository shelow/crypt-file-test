package unit.domain.usecases;

import domain.entities.CustomFile;
import org.junit.Test;
import domain.exceptions.MissingFileExsception;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class uploadFileTest {

    public static final String MON_FICHIER_TXT = "mon_fichier.txt";
    public static final String SRC_TEST_RESOURCES = "src/test/resources/";

    @Test(expected = MissingFileExsception.class)
    public void upload_null_file_should_throw_missing_file_exception(){
        //GIVEN
        UploadFile uploadFile = new UploadFile(new InMemoryFileDirectory());

        //WHEN
        uploadFile.handle(null);
    }

    @Test
    public void upload_file_should_save_it_in_directory() throws FileNotFoundException {
        //GIVEN
        InMemoryFileDirectory fileDirectory = new InMemoryFileDirectory();
        UploadFile uploadFile = new UploadFile(fileDirectory);

        //WHEN
        uploadFile.handle(new CustomFile(MON_FICHIER_TXT, new FileInputStream(SRC_TEST_RESOURCES + MON_FICHIER_TXT)));

        //THEN
        boolean found = fileDirectory.exists("mon_fichier.txt");
        assertThat(found, is(true));
    }
}
