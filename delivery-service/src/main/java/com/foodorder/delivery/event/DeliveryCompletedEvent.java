package com.foodorder.delivery.event;

public record DeliveryCompletedEvent(
    Long orderId,
    String status
) {}
