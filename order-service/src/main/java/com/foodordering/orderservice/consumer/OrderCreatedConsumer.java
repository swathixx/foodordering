package com.foodordering.orderservice.consumer;

import com.foodordering.orderservice.event.OrderCreatedEvent;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderCreatedConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedConsumer.class);

    private final RuntimeService runtimeService;

    public OrderCreatedConsumer(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @JmsListener(destination = "${order.created.queue.name}")
    public void consumeOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent from queue for order ID: {}", event.orderId());
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("orderId", event.orderId());
            
            // Start Camunda Process Instance using the order ID as the business key
            runtimeService.startProcessInstanceByKey("food-ordering-process", String.valueOf(event.orderId()), variables);
            log.info("Successfully started Camunda workflow for order ID: {}", event.orderId());
        } catch (Exception e) {
            log.error("Failed to start Camunda workflow for order ID: {}", event.orderId(), e);
        }
    }
}
