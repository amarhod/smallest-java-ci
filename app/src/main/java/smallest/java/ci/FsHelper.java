package smallest.java.ci;


import org.eclipse.jgit.util.FileUtils;

import java.io.File;
import java.io.IOException;


/** File system helper class */
public class FsHelper {
	/** Removes a folder at the given filepath */
    static void removeFolder(String filepath) throws IOException {
        File dir = new File(filepath);
        try {
            FileUtils.delete(dir, FileUtils.RECURSIVE);
            } catch (IOException e) {}
    }
}
