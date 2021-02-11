package smallest.java.ci;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static smallest.java.ci.FsHelper.removeFolder;
import static smallest.java.ci.GitHelper.cloneRepo;

public class GitHelperTest {
    @Test
    void cloneRepoTest() throws IOException, GitAPIException {
        File newDirectory = new File("testFolder");
        boolean isCreated = newDirectory.mkdirs();
        if (!isCreated) {
            removeFolder("testFolder");
            throw new IOException("Folder already exists");
        }
        cloneRepo("testFolder", "webhook");
        Assertions.assertNotEquals(0, new File("testFolder").list().length);
        removeFolder("testFolder");
    }
    
    @Test
    void cloneRepoExceptionTest() throws IOException {
        File newDirectory = new File("testFolder2");
        boolean isCreated = newDirectory.mkdirs();
        if (!isCreated) {
            removeFolder("testFolder2");
            throw new IOException("Folder already exists");
        }
        Assertions.assertThrows(GitAPIException.class, () ->{
            cloneRepo("testFolder2", "nonExistingBranch");
        });
        removeFolder("testFolder2");
    }
}
