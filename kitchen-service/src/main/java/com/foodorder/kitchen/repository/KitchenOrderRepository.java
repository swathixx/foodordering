package com.foodorder.kitchen.repository;

import com.foodorder.kitchen.model.KitchenOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KitchenOrderRepository extends JpaRepository<KitchenOrder, Long> {
    Optional<KitchenOrder> findByOrderId(Long orderId);
}
