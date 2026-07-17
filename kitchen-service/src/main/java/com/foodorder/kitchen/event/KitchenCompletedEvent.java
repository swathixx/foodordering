package com.foodorder.kitchen.event;

public record KitchenCompletedEvent(
    Long orderId,
    String status
) {}
