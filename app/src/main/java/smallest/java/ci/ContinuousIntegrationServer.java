package smallest.java.ci;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.File; 
import java.io.*;
import java.util.stream.Collectors;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.simple.parser.ParseException;

import static smallest.java.ci.GitHelper.*;

/** 
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
*/
public class ContinuousIntegrationServer extends AbstractHandler
{
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException {

        String requestMethod = request.getMethod();
        System.out.printf("handling %s request to URI: %s\n", requestMethod, target);
        switch (requestMethod){
            case "GET":
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
                response.getWriter().println("CI job done");
		        break;

            case "POST":

	        	//Collects the webook data which you can pick and choose info from to add to the mail
                String payload = request.getReader().lines().collect(Collectors.joining());

                boolean isValid = false;
                try {
                    isValid = isValidWebhook(payload, "assessment");
                } catch (ParseException e) {e.printStackTrace();}

                if(isValid) {
                    try {
                        cloneRepo("version/smallest-java-ci", "assessment");
                    } catch (GitAPIException e) {
                        e.printStackTrace();
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        baseRequest.setHandled(true);
                        return;
                    }
		    
                    File gitfolder = new File("version/smallest-java-ci");
                    Runtime rt = Runtime.getRuntime();
                    Process proc = rt.exec("./gradlew clean build --warning-mode none", null, gitfolder);
                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                    //prints build status to stdout
                    System.out.println("\n###BUILDING REPOSITORY####\n");
                    String s = null;
                    while ((s = stdInput.readLine()) != null) {
                        System.out.println(s);
                    }
                    System.out.println("\n###BUILD FINISHED####\n");

                    MailNotification.sendMail(payload);

                    response.setStatus(HttpServletResponse.SC_OK);
                    baseRequest.setHandled(true);
                }else{
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    baseRequest.setHandled(true);
                }
		        break;
        }
    }
 
    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer()); 
        server.start();
        server.join();
    }
}
