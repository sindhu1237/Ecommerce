package com.app.ecomapplication.controller;
import com.app.ecomapplication.model.EmailRequest;
import com.app.ecomapplication.service.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/emails")
public class EmailController {
    private final EmailService emailService;
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
     }
     @PostMapping("/send")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        emailService.sendEmail(emailRequest);
        return "Email sent successfully";
    }
}
