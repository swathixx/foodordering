package com.foodordering.orderservice.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentCompletedEvent(
    Long paymentId,
    Long orderId,
    String customerName,
    String foodItem,
    BigDecimal amount,
    String status,
    LocalDateTime completedAt
) {}
