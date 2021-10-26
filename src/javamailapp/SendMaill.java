package javamailapp;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SendMaill
{
    Session newSession = null;
    MimeMessage mimeMessage = null;
    String user; 
    String pass; 
    String recipients; 
    String subject; 
    String body;
    
    public SendMaill(){}
    public SendMaill(String user, String pass, String recipients, String subject, String body){
        this.user = user;
        this.pass = pass;
        this.recipients = recipients;
        this.subject = subject;
        this.body = body;
    }
    public void run() throws AddressException, MessagingException, IOException
    {
        SendMaill mail = new SendMaill(user, pass, recipients, subject, body);
        mail.setupServerProperties();
        mail.draftEmail();
        mail.sendEmail();
    }
    private void sendEmail() throws MessagingException {
        String emailHost = "smtp.gmail.com";
        Transport transport = newSession.getTransport("smtp");
        transport.connect(emailHost, user, pass);
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        transport.close();
        System.out.println("Email successfully sent!!!");
    }
    private MimeMessage draftEmail() throws AddressException, MessagingException, IOException {
        mimeMessage = new MimeMessage(newSession);
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
        mimeMessage.setSubject(subject);
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(body,"html/text");
        MimeMultipart multiPart = new MimeMultipart();
        multiPart.addBodyPart(bodyPart);
        mimeMessage.setContent(multiPart);
        return mimeMessage;
    }
    private void setupServerProperties() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        newSession = Session.getDefaultInstance(properties,null);
    }
	
}	
