package com.foodorder.kitchen.consumer;

import com.foodorder.kitchen.event.KitchenPendingEvent;
import com.foodorder.kitchen.model.KitchenOrder;
import com.foodorder.kitchen.model.KitchenStatus;
import com.foodorder.kitchen.repository.KitchenOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class KitchenEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(KitchenEventConsumer.class);

    private final KitchenOrderRepository kitchenOrderRepository;

    public KitchenEventConsumer(KitchenOrderRepository kitchenOrderRepository) {
        this.kitchenOrderRepository = kitchenOrderRepository;
    }

    @JmsListener(destination = "${kitchen.pending.queue.name}")
    public void consumeKitchenPending(KitchenPendingEvent event) {
        log.info("Received KitchenPendingEvent for order ID: {}", event.orderId());
        try {
            // Save new kitchen order with status PREPARING
            KitchenOrder kitchenOrder = KitchenOrder.builder()
                    .orderId(event.orderId())
                    .customerName(event.customerName())
                    .foodItem(event.foodItem())
                    .status(KitchenStatus.PREPARING)
                    .build();

            kitchenOrderRepository.save(kitchenOrder);
            log.info("Saved KitchenOrder with status PREPARING for order ID: {}", event.orderId());
        } catch (Exception e) {
            log.error("Failed to process kitchen pending event for order ID: {}", event.orderId(), e);
        }
    }
}
