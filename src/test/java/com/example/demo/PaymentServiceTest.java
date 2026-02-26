package com.example.demo;

import com.example.demo.dto.CreatePaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.domain.PaymentStatus;
import com.example.demo.service.PaymentService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Test
    void shouldCreatePaymentSuccessfully() {

        CreatePaymentRequest request =
                new CreatePaymentRequest(BigDecimal.valueOf(100), "Compra normal");

        PaymentResponse response = paymentService.createPayment(request);

        assertNotNull(response.getId());
        assertEquals(BigDecimal.valueOf(100), response.getAmount());
        assertEquals(PaymentStatus.PENDING, response.getStatus());
    }

    @Test
    void shouldApproveLowRiskPayment() {

        CreatePaymentRequest request =
                new CreatePaymentRequest(BigDecimal.valueOf(50), "Compra simples");

        PaymentResponse created = paymentService.createPayment(request);
        paymentService.analyzePayment(created.getId());
        PaymentResponse decided = paymentService.autoDecide(created.getId());

        assertEquals(PaymentStatus.APPROVED, decided.getStatus());
    }

    @Test
    void shouldRejectHighRiskPayment() {

        CreatePaymentRequest request =
                new CreatePaymentRequest(BigDecimal.valueOf(6000), "Pagamento urgente");

        PaymentResponse created = paymentService.createPayment(request);
        paymentService.analyzePayment(created.getId());
        PaymentResponse decided = paymentService.autoDecide(created.getId());

        assertEquals(PaymentStatus.REJECTED, decided.getStatus());
    }
}