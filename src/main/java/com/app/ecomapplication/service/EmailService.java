package com.app.ecomapplication.service;

import com.app.ecomapplication.model.EmailRequest;

public interface EmailService {
    void sendEmail(EmailRequest request);
}
