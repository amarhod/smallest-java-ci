package smallest.java.ci;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.json.JSONObject;

public class mailNotification {
	
	static void sendMail(String requestData) {
		
		JSONObject obj = new JSONObject(requestData);
        
        String name = obj.getJSONObject("pusher").getString("name");
        String repo = obj.getJSONObject("repository").getString("name");
        
    	String username = "dd2480lab2@gmail.com";
	    String password = "2480Lab2!";
	    String recipient = "pjanse@kth.se";
	    
	    Properties props = new Properties();

	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.from", "dd2480lab2@gmail.com");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

	    Session session = Session.getInstance(props, null);
	    MimeMessage msg = new MimeMessage(session);

	    try {
    	    msg.setRecipients(Message.RecipientType.TO, recipient);
    	    msg.setSubject("A POST have been made");
    	    msg.setSentDate(new Date());
    	    msg.setText("User: " + name + " made a commit to " + repo);

			Transport transport = session.getTransport("smtp");
    	    transport.connect(username, password);
    	    transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
	    } catch (MessagingException e) {
		e.printStackTrace();
	    }
		
		
		
	}
}
