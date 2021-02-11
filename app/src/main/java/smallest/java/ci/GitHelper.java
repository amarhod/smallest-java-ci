package smallest.java.ci;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

import static java.util.Collections.singleton;
import static smallest.java.ci.FsHelper.removeFolder;

class GitHelper {

    static void cloneRepo(String filepath, String branch) throws GitAPIException {
        try {removeFolder(filepath);} catch (IOException ioException) {}
            Git.cloneRepository()
                .setURI("https://github.com/amarhod/smallest-java-ci")
                .setDirectory(new File(filepath))
                .setBranchesToClone(singleton("refs/heads/" + branch))
                .setBranch("refs/heads/" + branch)
                .call().close();
        }

}
