package com.foodorder.payment.dto;

import com.foodorder.payment.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {
    private Long id;
    private Long orderId;
    private String customerName;
    private String foodItem;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime createdAt;
}
