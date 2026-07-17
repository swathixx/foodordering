package com.foodordering.orderservice.consumer;

import com.foodordering.orderservice.event.DeliveryCompletedEvent;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class DeliveryCompletedConsumer {

    private static final Logger log = LoggerFactory.getLogger(DeliveryCompletedConsumer.class);

    private final RuntimeService runtimeService;

    public DeliveryCompletedConsumer(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @JmsListener(destination = "delivery.completed")
    public void consumeDeliveryCompleted(DeliveryCompletedEvent event) {
        log.info("Received DeliveryCompletedEvent for order ID: {}, status: {}", event.orderId(), event.status());
        try {
            runtimeService.createMessageCorrelation("deliveryCompletedMessage")
                    .processInstanceBusinessKey(String.valueOf(event.orderId()))
                    .correlate();
            log.info("Successfully correlated deliveryCompletedMessage for order ID: {}", event.orderId());
        } catch (Exception e) {
            log.error("Failed to correlate deliveryCompletedMessage for order ID: {}", event.orderId(), e);
        }
    }
}
