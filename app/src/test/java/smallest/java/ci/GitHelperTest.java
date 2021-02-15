package smallest.java.ci;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

import static smallest.java.ci.FsHelper.removeFolder;
import static smallest.java.ci.GitHelper.cloneRepo;
import static smallest.java.ci.GitHelper.isValidWebhook;

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

    @Test
    void isValidWebhookTest() throws IOException, ParseException {
        String payload;
        String currentPath = System.getProperty("user.dir") + "/src/test/java/smallest/java/ci/";
        String filepath = currentPath + "payload.json";
        try {
            payload = new BufferedReader(new FileReader(filepath)).lines().collect(Collectors.joining());
        }catch(IOException e){throw new IOException("payload test file missing");}

        Assertions.assertEquals(true, isValidWebhook(payload, "webhook-signals"));

        Assertions.assertEquals(false, isValidWebhook(payload, "nonExistingBranch"));

        filepath = currentPath + "badPayload.json";
        try {
            payload = new BufferedReader(new FileReader(filepath)).lines().collect(Collectors.joining());
        }catch(IOException e){throw new IOException("payload test file missing");}

        Assertions.assertEquals(false, isValidWebhook(payload, "nonExistingBranch"));
    }
}
