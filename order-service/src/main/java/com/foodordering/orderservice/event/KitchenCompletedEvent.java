package com.foodordering.orderservice.event;

public record KitchenCompletedEvent(
    Long orderId,
    String status
) {}
