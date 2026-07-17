package com.foodorder.payment.consumer;

import com.foodorder.payment.dto.PaymentRequestDto;
import com.foodorder.payment.dto.PaymentResponseDto;
import com.foodorder.payment.event.OrderCreatedEvent;
import com.foodorder.payment.model.PaymentStatus;
import com.foodorder.payment.publisher.PaymentEventPublisher;
import com.foodorder.payment.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedConsumer.class);

    private final PaymentService paymentService;
    private final PaymentEventPublisher paymentEventPublisher;

    public OrderCreatedConsumer(PaymentService paymentService, PaymentEventPublisher paymentEventPublisher) {
        this.paymentService = paymentService;
        this.paymentEventPublisher = paymentEventPublisher;
    }

    @JmsListener(destination = "${order.created.queue.name}")
    public void consumeOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for order ID: {}", event.orderId());
        try {
            // Process payment logic: create payment with SUCCESS status
            PaymentRequestDto requestDto = PaymentRequestDto.builder()
                    .orderId(event.orderId())
                    .customerName(event.customerName())
                    .foodItem(event.foodItem())
                    .amount(event.amount())
                    .status(PaymentStatus.SUCCESS)
                    .build();

            PaymentResponseDto responseDto = paymentService.createPayment(requestDto);
            log.info("Payment processed successfully for order ID: {}, payment ID: {}", event.orderId(), responseDto.getId());

            // Publish payment completed event
            paymentEventPublisher.publishPaymentCompleted(responseDto);
        } catch (Exception e) {
            log.error("Failed to process payment for order ID: {}", event.orderId(), e);
        }
    }
}
