package com.app.ecomapplication.controller;

import com.app.ecomapplication.model.PaymentRequest;
import com.app.ecomapplication.model.PaymentResponse;
import com.app.ecomapplication.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(
            PaymentService paymentService
    ) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-link")
    public PaymentResponse createPaymentLink(
            @RequestBody PaymentRequest request
    ) {

        String url =
                paymentService.createPaymentLink(
                        request.getCartId()
                );

        PaymentResponse response =
                new PaymentResponse();

        response.setPaymentUrl(url);

        return response;
    }
}