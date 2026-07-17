package com.foodorder.delivery.consumer;

import com.foodorder.delivery.event.DeliveryPendingEvent;
import com.foodorder.delivery.model.Delivery;
import com.foodorder.delivery.model.DeliveryStatus;
import com.foodorder.delivery.repository.DeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class DeliveryEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(DeliveryEventConsumer.class);

    private final DeliveryRepository deliveryRepository;

    public DeliveryEventConsumer(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @JmsListener(destination = "${delivery.pending.queue.name}")
    public void consumeDeliveryPending(DeliveryPendingEvent event) {
        log.info("Received DeliveryPendingEvent for order ID: {}", event.orderId());
        try {
            // Save new delivery record with status DELIVERING
            Delivery delivery = Delivery.builder()
                    .orderId(event.orderId())
                    .customerName(event.customerName())
                    .foodItem(event.foodItem())
                    .status(DeliveryStatus.DELIVERING)
                    .build();

            deliveryRepository.save(delivery);
            log.info("Saved Delivery record with status DELIVERING for order ID: {}", event.orderId());
        } catch (Exception e) {
            log.error("Failed to process delivery pending event for order ID: {}", event.orderId(), e);
        }
    }
}
