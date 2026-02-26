package com.example.demo.dto;

import com.example.demo.domain.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Data
public class PaymentResponse {

    private UUID id;
    private BigDecimal amount;
    private String description;
    private PaymentStatus status;

    private Integer riskScore;
    private List<String> riskReasons;
}