package com.foodordering.orderservice.event;

public record DeliveryCompletedEvent(
    Long orderId,
    String status
) {}
