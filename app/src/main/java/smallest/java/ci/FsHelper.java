package smallest.java.ci;


import org.eclipse.jgit.util.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;


/** File system helper class */
public class FsHelper {

  	/** Removes a folder at the given filepath */
    static void removeFolder(String filepath) throws IOException {
        File dir = new File(filepath);
        try {
            FileUtils.delete(dir, FileUtils.RECURSIVE);
            } catch (IOException e) {}
    }
  
  	/** Stores information for a specifc commit and its build result onto a local file */
    static void saveBuildInfo(String gradleBuildInfo, String payload, String commitHash) throws IOException {
        JSONParser parser = new JSONParser();
        JSONObject payloadObject;
        try {
            payloadObject = (JSONObject) parser.parse(payload);
        }catch(ParseException e){return;}
        JSONObject headCommit = (JSONObject) payloadObject.get("head_commit");
        String commitMsg = (String) headCommit.get("message");
        JSONObject pusher = (JSONObject) headCommit.get("author");
        String author = (String) pusher.get("name");

        String commitSummary = "\nAuthor: " + author + "\nCommit: " + commitHash + "\nCommit message: " + commitMsg + '\n';
        String finalSummary =  commitSummary + gradleBuildInfo;
        File buildMeta = new File("buildMetaInfo/" + commitHash);
        try (PrintWriter out = new PrintWriter("buildMetaInfo/" + commitHash)){
            out.println(finalSummary + '\n');
        }
    }
}
