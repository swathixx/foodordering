package com.foodordering.orderservice.event;

public record KitchenPendingEvent(
    Long orderId,
    String customerName,
    String foodItem
) {}
