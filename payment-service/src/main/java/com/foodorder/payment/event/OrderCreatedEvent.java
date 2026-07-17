package com.foodorder.payment.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderCreatedEvent(
    Long orderId,
    String customerName,
    String foodItem,
    BigDecimal amount,
    String status,
    LocalDateTime createdAt
) {}
