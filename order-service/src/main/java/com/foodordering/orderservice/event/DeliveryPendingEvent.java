package com.foodordering.orderservice.event;

public record DeliveryPendingEvent(
    Long orderId,
    String customerName,
    String foodItem
) {}
