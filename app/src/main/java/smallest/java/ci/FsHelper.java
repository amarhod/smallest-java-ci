package smallest.java.ci;


import org.eclipse.jgit.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class FsHelper {
    static void removeFolder(String filepath) throws IOException {
        File dir = new File(filepath);
        try {
            FileUtils.delete(dir, FileUtils.RECURSIVE);
            } catch (IOException e) {}
    }
}
