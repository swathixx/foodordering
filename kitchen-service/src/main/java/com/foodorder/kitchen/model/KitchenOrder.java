package com.foodorder.kitchen.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "kitchen_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KitchenOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Column(name = "food_item", nullable = false, length = 100)
    private String foodItem;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private KitchenStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
