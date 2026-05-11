package com.app.ecomapplication.util;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {
    public static void sendEmail(Session session, String toEmail, String subject, String body) {
        // Implement email sending logic using JavaMail API
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("YOUR_GMAIL@gmail.com", false));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(body, "text/html");
            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail);
        }catch(MessagingException e){
            throw new RuntimeException(e);
        }
    }
}
