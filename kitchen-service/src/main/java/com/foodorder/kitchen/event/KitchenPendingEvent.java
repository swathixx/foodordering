package com.foodorder.kitchen.event;

public record KitchenPendingEvent(
    Long orderId,
    String customerName,
    String foodItem
) {}
