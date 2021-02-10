package smallest.java.ci;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.util.FS;

import java.io.File;
import java.io.IOException;

class GitHelper {
    static Repository getRepo(String filepath) throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repo = builder.setGitDir(new File(filepath))
            .readEnvironment()
            .findGitDir()
            .build();
        return repo;
    }

    static boolean hasAtLeastOneRef(Repository repo){
        for(Ref ref: repo.getAllRefs().values()){
            if(ref.getObjectId() != null)
                return true;
        }
        return false;
    }
    static boolean isGitRepo(String filepath) throws IOException {
        if(RepositoryCache.FileKey.isGitRepository(new File(filepath), FS.DETECTED)) {
            System.out.println("Repo detected");
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repo = getRepo(filepath);
            if (hasAtLeastOneRef(repo))
                return true;
        }
        return false;
    }

    static void cloneOrPull(String filepath) throws GitAPIException, InvalidRemoteException, TransportException, IOException {
        boolean isRepo = false;
        try {
            isRepo = isGitRepo(filepath);
        }catch (IOException e){e.printStackTrace();}
        if(true){
            String branch = "webhook-signals";
            Repository repo = getRepo(filepath);
            System.out.println(repo.getDirectory());
            Git git = new Git(repo);
            git.checkout().setName(branch).setStartPoint("origin/" + branch).call();
            git.pull().call();
        }else {
            Git.cloneRepository()
                    .setURI("https://github.com/amarhod/smallest-java-ci")
                    .setDirectory(new File(filepath))
                    .call();
        }
    }
}
