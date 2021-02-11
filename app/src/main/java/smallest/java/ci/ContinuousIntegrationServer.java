package smallest.java.ci;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
 
import java.io.IOException;
 
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jgit.api.errors.GitAPIException;

import static smallest.java.ci.GitHelper.cloneRepo;

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
                try {
                    cloneRepo("version/smallest-java-ci", "webhook-signals");
                } catch (GitAPIException e) {e.printStackTrace();}
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
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
