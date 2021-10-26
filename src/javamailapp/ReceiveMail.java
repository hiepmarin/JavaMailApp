/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javamailapp;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Properties;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
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
    
    public static void getMailWithPOP3() throws MessagingException, IOException {
        try {
            Properties pro = System.getProperties();
        pro.put("mail.pop3.host", "pop.gmail.com");
        pro.put("mail.pop3.port", "995");
        pro.put("mail.store.protocol", "pop3");
        pro.put("mail.pop3.socketFactory.class", javax.net.ssl.SSLSocketFactory.class.getName());
        
        Session session = Session.getDefaultInstance(pro, new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("tranlochiep13@gmail.com", "LHiep1009");
            }
        });
        
        Store store = session.getStore();
        store.connect();
        
        Folder[] folders = store.getDefaultFolder().list("*");
        for(Folder folder : folders){
            if(!folder.isOpen()){
                folder.open(Folder.READ_ONLY);
            }
            System.out.println(" #[" + folder.getFullName() + "]");
            
            Message[] messages = folder.getMessages();
            if(messages.length < 1){
                System.out.println("0 emails found in INBOX");
            }
            else{
                System.out.println(messages.length + " emails found in INBOX");
            }
            for(Message message : messages) {
                String from = "";
                InternetAddress[] addresses = (InternetAddress[]) message.getFrom();
                for(InternetAddress address : addresses){
                    from += address.getAddress();
                }
                
                System.out.println(" + From: " + from);
                System.out.println(" + Subject: " + message.getSubject());
                System.out.println(" + Body : " + getTextFromMessage(message));
                System.out.println(" + Time : " + message.getSentDate());
                System.out.println("");            
            }
        }
        }catch(AuthenticationFailedException ex){
            System.out.println("Error: Your account is not enabled for POP access. Please visit your Gmail settings page and enable your account for POP access.");
        }
        
    }
    
    private static String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = "**can't read multipart type**";
        }
        return result;
    }
    
    public static void main(String arg[]) throws MessagingException, IOException {
        
        
        try{
            System.out.println("Get mail...");
            getMailWithPOP3();
            System.out.println("Done!!");
        }catch(IOException ex){
            
        }
        
//        try {
//            System.out.println("Get mail...");
//            getMailWithPOP3();
//            System.out.println("Error!!");
//        }
//        catch (NoSuchProviderException ex){
//            Logger.getLogger(MailClient.class.getName()).log(Level.SEVERE, null);
//        }
    }
}
