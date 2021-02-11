package smallest.java.ci;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static smallest.java.ci.FsHelper.removeFolder;

public class FsHelperTest {
    @Test
    void removeFolderTest() throws IOException {
        File newDirectory = new File("testFolder");
        boolean isCreated = newDirectory.mkdirs();
        if(!isCreated){throw new IOException("Folder already exists"); }
        removeFolder("testFolder");
        Assertions.assertEquals(false, new File("testFolder").exists()); }
}
