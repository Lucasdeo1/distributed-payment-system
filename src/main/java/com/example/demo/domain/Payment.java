package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime createdAt;

    // 🔽 NOVOS CAMPOS

    private Integer riskScore;

    @ElementCollection
    @CollectionTable(name = "payment_risk_reasons",
            joinColumns = @JoinColumn(name = "payment_id"))
    @Column(name = "reason")
    private List<String> riskReasons;
}