package com.foodordering.orderservice.publisher;

import com.foodordering.orderservice.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(OrderEventPublisher.class);

    private final JmsTemplate jmsTemplate;
    private final String orderCreatedQueue;

    public OrderEventPublisher(JmsTemplate jmsTemplate,
                               @Value("${order.created.queue.name}") String orderCreatedQueue) {
        this.jmsTemplate = jmsTemplate;
        this.orderCreatedQueue = orderCreatedQueue;
    }

    public void publishOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Publishing OrderCreatedEvent to queue [{}]: {}", orderCreatedQueue, event);
        jmsTemplate.convertAndSend(orderCreatedQueue, event);
        log.info("Successfully published OrderCreatedEvent to queue [{}]: {}", orderCreatedQueue, event);
    }
}
