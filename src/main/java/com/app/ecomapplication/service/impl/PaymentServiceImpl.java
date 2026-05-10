package com.app.ecomapplication.service.impl;
import com.app.ecomapplication.model.*;
import com.app.ecomapplication.repository.CartRepository;
import com.app.ecomapplication.repository.PaymentRepository;
import com.app.ecomapplication.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;
@Service
public class PaymentServiceImpl
        implements PaymentService {
    private final CartRepository cartRepository;
    private final PaymentRepository paymentRepository;
    public PaymentServiceImpl(
            CartRepository cartRepository,
            PaymentRepository paymentRepository
    ) {
        this.cartRepository = cartRepository;
        this.paymentRepository = paymentRepository;
    }
    @Override
    public String createPaymentLink(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cart not found"
                        ));
        double totalAmount =
                cart.getProducts()
                        .stream()
                        .mapToDouble(item ->
                                item.getQuantity() * 100
                        )
                        .sum();
        try {

            SessionCreateParams.LineItem.PriceData.ProductData
                    productData =
                    SessionCreateParams.LineItem
                            .PriceData
                            .ProductData.builder()
                            .setName("Cart Payment")
                            .build();

            SessionCreateParams.LineItem.PriceData
                    priceData =
                    SessionCreateParams.LineItem
                            .PriceData.builder()
                            .setCurrency("inr")
                            .setUnitAmount(
                                    (long) totalAmount * 100
                            )
                            .setProductData(productData)
                            .build();

            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem
                            .builder()
                            .setQuantity(1L)
                            .setPriceData(priceData)
                            .build();

            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .setMode(
                                    SessionCreateParams
                                            .Mode.PAYMENT
                            )
                            .setSuccessUrl(
                                    "http://localhost:8080/payment-success"
                            )
                            .setCancelUrl(
                                    "http://localhost:8080/payment-failed"
                            )
                            .addLineItem(lineItem)
                            .build();

            Session session =
                    Session.create(params);

            Payment payment = new Payment();

            payment.setCartId(cartId);

            payment.setAmount(totalAmount);

            payment.setPaymentLink(session.getUrl());

            payment.setPaymentGatewayReferenceId(
                    session.getId()
            );
            payment.setPaymentStatus(
                    PaymentStatus.PENDING
            );
            payment.setPaymentGateway(
                    PaymentGateway.STRIPE
            );
            paymentRepository.save(payment);
            return session.getUrl();
        } catch (StripeException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    e.getMessage()
            );
        }
    }
}