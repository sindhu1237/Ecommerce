package com.app.ecomapplication.repository;
import com.app.ecomapplication.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByPaymentGatewayReferenceId(String paymentGatewayReferenceId);
}