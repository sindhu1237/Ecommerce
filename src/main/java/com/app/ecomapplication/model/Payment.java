package com.app.ecomapplication.model;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "payments")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cartId;
    private Double amount;
    @Column(length = 1000)
    private String paymentLink;
    @Column(length = 500)
    private String paymentGatewayReferenceId;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    private PaymentGateway paymentGateway;
}