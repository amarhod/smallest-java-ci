package smallest.java.ci;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import static java.util.Collections.singleton;
import static smallest.java.ci.FsHelper.removeFolder;

/** Git helper class containing methods that use JGit*/
class GitHelper {
  
    /** Returns the commit hash string from a webhook JSON payload */
    static String getCommitHash(String payload){
        JSONParser parser = new JSONParser();
        JSONObject payloadObject;
        try {
            payloadObject = (JSONObject) parser.parse(payload);
        }catch(ParseException e){return null;}
        String commitHash = (String) payloadObject.get("after");
        if(commitHash == null || commitHash == "")
            return null;
        return commitHash;
    }
	
	  /** Checks if the payload for the given post request is valid */
    static boolean isValidWebhook(String payload, String acceptedBranch) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject payloadObject;
        try {
            payloadObject = (JSONObject) parser.parse(payload);
        }catch(ParseException e){return false;}
        String branch = (String) payloadObject.get("ref");
        if(branch == null || !branch.equals("refs/heads/" + acceptedBranch))
            return false;
        return true;
    }
	
	  /** Clones the git repository and checks out the given branch into a folder at the given filepath */
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
