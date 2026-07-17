package com.foodorder.delivery.event;

public record DeliveryPendingEvent(
    Long orderId,
    String customerName,
    String foodItem
) {}
