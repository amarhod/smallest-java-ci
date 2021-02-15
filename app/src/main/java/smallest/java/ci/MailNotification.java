package smallest.java.ci;

import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.JSONObject;

/** Class that handles the sending of emails notifying users of their commit status */
public class MailNotification {
	
	static void sendMail(String requestData) {

		JSONObject obj = new JSONObject(requestData);
        
        String name = obj.getJSONObject("pusher").getString("name");
        String repo = obj.getJSONObject("repository").getString("name");
		String commitMsg = obj.getJSONObject("head_commit").getString("message");
		String branch = obj.getString("ref").replace("refs/heads/", "");

		String mailText = "User: " + name + " has made a commit on the branch: " + branch + "\n with commit message: " + commitMsg;

    	String username = "dd2480lab2@gmail.com";
	    String password = "2480Lab2!";
	    String recipient1 = "amarh@kth.se";
		String recipient2 = "pjanse@kth.se";
		String recipient3 = "gdowling@kth.se";

	    Properties props = new Properties();

		props.put("mail.smtp.auth", true);
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.from", username);
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

	    try {
			MimeMessage msg = new MimeMessage(session);
			msg.addRecipient(Message.RecipientType.TO,new InternetAddress(recipient1));
			msg.addRecipient(Message.RecipientType.TO,new InternetAddress(recipient2));
			msg.addRecipient(Message.RecipientType.TO,new InternetAddress(recipient3));
			msg.setSubject("A commit has been made to repository: " + repo);
    	    msg.setText(mailText);
			Transport.send(msg);
	    } catch (MessagingException e) {
		e.printStackTrace();
	    }
		
		
		
	}
}
