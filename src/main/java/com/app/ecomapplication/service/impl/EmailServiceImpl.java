package com.app.ecomapplication.service.impl;
import com.app.ecomapplication.model.EmailRequest;
import com.app.ecomapplication.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Service
public class EmailServiceImpl implements EmailService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    public EmailServiceImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }
    public void sendEmail(EmailRequest request){
        try{
            String message = objectMapper.writeValueAsString(request);
            kafkaTemplate.send("sendEmail", message);
        }catch(JsonProcessingException e){
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
