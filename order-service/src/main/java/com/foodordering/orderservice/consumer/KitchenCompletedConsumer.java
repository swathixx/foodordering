package com.foodordering.orderservice.consumer;

import com.foodordering.orderservice.event.KitchenCompletedEvent;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class KitchenCompletedConsumer {

    private static final Logger log = LoggerFactory.getLogger(KitchenCompletedConsumer.class);

    private final RuntimeService runtimeService;

    public KitchenCompletedConsumer(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @JmsListener(destination = "${kitchen.completed.queue.name}")
    public void consumeKitchenCompleted(KitchenCompletedEvent event) {
        log.info("Received KitchenCompletedEvent for order ID: {}, status: {}", event.orderId(), event.status());
        try {
            runtimeService.createMessageCorrelation("kitchenCompletedMessage")
                    .processInstanceBusinessKey(String.valueOf(event.orderId()))
                    .correlate();
            log.info("Successfully correlated kitchenCompletedMessage for order ID: {}", event.orderId());
        } catch (Exception e) {
            log.error("Failed to correlate kitchenCompletedMessage for order ID: {}", event.orderId(), e);
        }
    }
}
