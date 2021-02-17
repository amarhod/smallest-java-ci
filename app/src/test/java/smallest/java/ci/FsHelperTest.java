package smallest.java.ci;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

import static smallest.java.ci.FsHelper.removeFolder;
import static smallest.java.ci.FsHelper.saveBuildInfo;
import static smallest.java.ci.GitHelper.getCommitHash;

public class FsHelperTest {

    /** Test that removeFolder removes the folder as intended */
    @Test
    void removeFolderTest() throws IOException {
        File newDirectory = new File("testFolder");
        boolean isCreated = newDirectory.mkdirs();
        if(!isCreated){throw new IOException("Folder already exists"); }
        removeFolder("testFolder");
        Assertions.assertFalse(new File("testFolder").exists());
    }

    /** Test that saveBuildInfo creates a non-empty file in the correct folder (app/buildMetaInfo) */
    @Test
    void saveBuildInfoTest() throws IOException {
        String currentPath = System.getProperty("user.dir") + "/src/test/java/smallest/java/ci/";
        String filepath = currentPath + "payload.json";
        Assertions.assertTrue(new File(filepath).exists());
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String payload = reader.lines().collect(Collectors.joining("\n"));
        String commitHash = getCommitHash(payload);
        Assertions.assertNotNull(commitHash);

        filepath = currentPath + "buildInfo";
        Assertions.assertTrue(new File(filepath).exists());
        reader = new BufferedReader(new FileReader(filepath));
        String buildInfo = reader.lines().collect(Collectors.joining("\n"));

        saveBuildInfo(buildInfo, payload, commitHash);
        File file = new File("buildMetaInfo/" + commitHash);
        Assertions.assertTrue(file.exists());
        Assertions.assertNotEquals(0, file.length());

        try{
            file.delete();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
