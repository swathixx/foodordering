package com.foodorder.payment.publisher;

import com.foodorder.payment.dto.PaymentResponseDto;
import com.foodorder.payment.event.PaymentCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PaymentEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventPublisher.class);

    private final JmsTemplate jmsTemplate;
    private final String paymentCompletedQueue;

    public PaymentEventPublisher(JmsTemplate jmsTemplate,
                                 @Value("${payment.completed.queue.name}") String paymentCompletedQueue) {
        this.jmsTemplate = jmsTemplate;
        this.paymentCompletedQueue = paymentCompletedQueue;
    }

    public void publishPaymentCompleted(PaymentResponseDto payment) {
        PaymentCompletedEvent event = new PaymentCompletedEvent(
                payment.getId(),
                payment.getOrderId(),
                payment.getCustomerName(),
                payment.getFoodItem(),
                payment.getAmount(),
                payment.getStatus().name(),
                LocalDateTime.now()
        );
        log.info("Publishing PaymentCompletedEvent to queue [{}]: {}", paymentCompletedQueue, event);
        jmsTemplate.convertAndSend(paymentCompletedQueue, event);
        log.info("Successfully published PaymentCompletedEvent for order ID: {}", payment.getOrderId());
    }
}
