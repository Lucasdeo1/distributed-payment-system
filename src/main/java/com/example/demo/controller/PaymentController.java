package com.example.demo.controller;

import com.example.demo.dto.CreatePaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(
            @Valid @RequestBody CreatePaymentRequest request) {

        PaymentResponse response = paymentService.createPayment(request);

        return ResponseEntity
                .created(URI.create("/payments/" + response.getId()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> list() {
        return ResponseEntity.ok(paymentService.listPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.getPayment(id));
    }

    @PostMapping("/{id}/analyze")
    public ResponseEntity<PaymentResponse> analyze(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.analyzePayment(id));
    }

    @PostMapping("/{id}/auto-decide")
    public ResponseEntity<PaymentResponse> autoDecide(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.autoDecide(id));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<PaymentResponse> approve(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.approve(id));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<PaymentResponse> reject(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.reject(id));
    }
}