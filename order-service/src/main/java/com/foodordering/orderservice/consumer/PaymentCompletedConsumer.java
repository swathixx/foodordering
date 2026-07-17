package com.foodordering.orderservice.consumer;

import com.foodordering.orderservice.event.PaymentCompletedEvent;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentCompletedConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentCompletedConsumer.class);

    private final RuntimeService runtimeService;

    public PaymentCompletedConsumer(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @JmsListener(destination = "${payment.completed.queue.name}")
    public void consumePaymentCompleted(PaymentCompletedEvent event) {
        log.info("Received PaymentCompletedEvent for order ID: {}, status: {}", event.orderId(), event.status());
        try {
            runtimeService.createMessageCorrelation("paymentCompletedMessage")
                    .processInstanceBusinessKey(String.valueOf(event.orderId()))
                    .setVariable("paymentStatus", event.status())
                    .correlate();
            log.info("Successfully correlated paymentCompletedMessage for order ID: {}", event.orderId());
        } catch (Exception e) {
            log.error("Failed to correlate paymentCompletedMessage for order ID: {}", event.orderId(), e);
        }
    }
}
