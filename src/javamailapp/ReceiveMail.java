/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javamailapp;

import java.io.IOException;
import java.util.Properties;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author hiept
 */
public class ReceiveMail {
    private String user = "";
    private String pass = "";
    private String inbox = "";
    
    public ReceiveMail(){};
    public ReceiveMail(String user, String pass){
        this.user = user;
        this.pass = pass;
    }
    
    public void getMailWithPOP3() throws MessagingException, IOException {
        try {
            inbox = "";
            Properties pro = System.getProperties();
            pro.put("mail.pop3.host", "pop.gmail.com");
            pro.put("mail.pop3.port", "995");
            pro.put("mail.store.protocol", "pop3");
            pro.put("mail.pop3.socketFactory.class", javax.net.ssl.SSLSocketFactory.class.getName());
        
            Session session = Session.getDefaultInstance(pro, new Authenticator(){
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, pass);
                }
            });
            Store store = session.getStore();
            store.connect();
        
            Folder[] folders = store.getDefaultFolder().list("*");
            for(Folder folder : folders){
                if(!folder.isOpen()){
                    folder.open(Folder.READ_ONLY);
                }
                inbox+=" #[" + folder.getFullName() + "]";
                Message[] messages = folder.getMessages();
                if(messages.length < 1){
                    inbox+="\n0 email found in INBOX";
                }
                else{
                    inbox+=messages.length + " emails found in INBOX\n";
                }
                int index = 1;
                for(Message message : messages) {
                    String from = "";
                    InternetAddress[] addresses = (InternetAddress[]) message.getFrom();
                    for(InternetAddress address : addresses){
                        from += address.getAddress();
                    }
                    inbox+="\nEmail " + index + " - " +  message.getSentDate();
                    inbox+="\n + From: " + from;
                    inbox+="\n + Subject: " + message.getSubject();
                    inbox+="\n + Body : " + getTextFromMessage(message) + "\n";
                    //inbox+=" + Time : " + message.getSentDate() + "\n";
                    index++;
                }
            }
        }catch(AuthenticationFailedException ex){
            System.out.println(ex.toString());
            inbox = "Error: Your account is not enabled for POP access. Please visit your Gmail settings page and enable your account for POP access.";
        }     
    }
    
    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        result = message.getContent().toString();
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            
            result = "**can't read multipart type**\n";
        }
        return result;
    }
    
    public String run() throws MessagingException, IOException {
        try{
            System.out.println("Get mail...\n");
            getMailWithPOP3();
            System.out.println("Done!!");
            System.out.println(inbox);
            return inbox;
        }catch(IOException ex){
            System.out.println("Error: " + ex.toString());
            return "Can't get mail!";
        }
    }
}
