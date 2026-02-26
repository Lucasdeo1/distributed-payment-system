package com.example.demo.service;

import com.example.demo.domain.Payment;
import com.example.demo.domain.PaymentStatus;
import com.example.demo.dto.CreatePaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.exception.PaymentNotFoundException;
import com.example.demo.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {

        Payment payment = Payment.builder()
                .amount(request.getAmount())
                .description(request.getDescription())
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return toResponse(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponse analyzePayment(UUID id) {

        Payment payment = getPaymentEntity(id);

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException("Somente pagamentos PENDENTES podem ser analisados.");
        }

        List<String> triggeredRules = new ArrayList<>();
        int score = calculateRiskScore(payment, triggeredRules);

        payment.setRiskScore(score);
        payment.setRiskReasons(triggeredRules);
        payment.setStatus(PaymentStatus.UNDER_REVIEW);

        return toResponse(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponse autoDecide(UUID id) {

        Payment payment = getPaymentEntity(id);

        if (payment.getStatus() != PaymentStatus.UNDER_REVIEW) {
            throw new IllegalStateException("Somente pagamentos em análise podem ser decididos automaticamente.");
        }

        if (payment.getRiskScore() != null && payment.getRiskScore() >= 70) {
            payment.setStatus(PaymentStatus.REJECTED);
        } else {
            payment.setStatus(PaymentStatus.APPROVED);
        }

        return toResponse(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponse approve(UUID id) {

        Payment payment = getPaymentEntity(id);

        if (payment.getStatus() != PaymentStatus.UNDER_REVIEW) {
            throw new IllegalStateException("Somente pagamentos em análise podem ser aprovados.");
        }

        payment.setStatus(PaymentStatus.APPROVED);

        return toResponse(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponse reject(UUID id) {

        Payment payment = getPaymentEntity(id);

        if (payment.getStatus() != PaymentStatus.UNDER_REVIEW) {
            throw new IllegalStateException("Somente pagamentos em análise podem ser rejeitados.");
        }

        payment.setStatus(PaymentStatus.REJECTED);

        return toResponse(paymentRepository.save(payment));
    }

    public PaymentResponse getPayment(UUID id) {
        return toResponse(getPaymentEntity(id));
    }

    public List<PaymentResponse> listPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Payment getPaymentEntity(UUID id) {
        return paymentRepository.findById(id)
                .orElseThrow(() ->
                        new PaymentNotFoundException("Pagamentos com o id " + id + " não encontrado")
                );
    }

    private int calculateRiskScore(Payment payment, List<String> triggeredRules) {

        int score = 0;

        // Regra 1: valor alto
        if (payment.getAmount().doubleValue() > 5000) {
            score += 50;
            triggeredRules.add("valor alto (> 5000)");
        }

        // Regra 2: horário suspeito (22h às 6h)
        int hour = payment.getCreatedAt().getHour();
        if (hour >= 22 || hour < 6) {
            score += 40;
            triggeredRules.add("Horário suspeito (22h - 6h)");
        }

        // Regra 3: palavra suspeita
        if (payment.getDescription() != null &&
                payment.getDescription().toLowerCase().contains("urgente")) {
            score += 20;
            triggeredRules.add("Palavra-chave suspeita: urgente");
        }

        return score;
    }

    private PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .description(payment.getDescription())
                .status(payment.getStatus())
                .riskScore(payment.getRiskScore())
                .riskReasons(payment.getRiskReasons())
                .build();
    }
}