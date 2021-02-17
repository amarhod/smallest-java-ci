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
import static smallest.java.ci.GitHelper.*;

public class GitHelperTest {
    
    /** Test that cloneRepo function creates a folder with files */
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
        Assertions.assertFalse(new File("testFolder").exists());
    }
    
    /** Test that cloneRepo function throws an exception when cloning from an nonexisting branch or repo */
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

    /** Test that the function isValidWebhook can differentiate between correct and uncorrect branches in payload. Also that it detects faulty payloads */
    @Test
    void isValidWebhookTest() throws IOException, ParseException {
        String payload;
        String currentPath = System.getProperty("user.dir") + "/src/test/java/smallest/java/ci/";
        String filepath = currentPath + "payload.json";
        try {
            payload = new BufferedReader(new FileReader(filepath)).lines().collect(Collectors.joining());
        }catch(IOException e){throw new IOException("payload test file missing");}

        Assertions.assertTrue(isValidWebhook(payload, "webhook-signals"));
        Assertions.assertFalse(isValidWebhook(payload, "nonExistingBranch"));

        filepath = currentPath + "badPayload.json";
        try {
            payload = new BufferedReader(new FileReader(filepath)).lines().collect(Collectors.joining());
        }catch(IOException e){throw new IOException("payload test file missing");}

        Assertions.assertFalse(isValidWebhook(payload, "nonExistingBranch"));
    }

    /** Test that getCommitHash returns correct commit hash from the payload that Github webhook sends */
    @Test
    void getCommitHashTest() throws IOException {
        String currentPath = System.getProperty("user.dir") + "/src/test/java/smallest/java/ci/";
        String filepath = currentPath + "payload.json";
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String payload = reader.lines().collect(Collectors.joining("\n"));
        String commitHash = getCommitHash(payload);
        Assertions.assertEquals("0762e2aff34081061608b9788ccee7a747a0355a", commitHash);
    }
}
