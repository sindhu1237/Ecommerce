package com.app.ecomapplication.kafka;
import com.app.ecomapplication.model.EmailRequest;
import com.app.ecomapplication.util.EmailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Properties;
@Service
public class SendEmailEventConsumer {
    private final ObjectMapper objectMapper;
    public SendEmailEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @KafkaListener(topics="sendEmail",groupId="emailService")
    public void handleSendEmailEvent(String message) throws JsonProcessingException{
        EmailRequest emailRequest = objectMapper.readValue(message, EmailRequest.class);
        System.out.println("Received email request: " + emailRequest);
        String to = emailRequest.getTo();
        String body = emailRequest.getBody();
        String subject = emailRequest.getSubject();
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        Authenticator auth = new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication("YOUR_GMAIL@gmail.com", "YOUR_APP_PASSWORD");
            }
        };
        Session session = Session.getInstance(props, auth);
        EmailUtil.sendEmail(session, to, subject, body);
    }
}
