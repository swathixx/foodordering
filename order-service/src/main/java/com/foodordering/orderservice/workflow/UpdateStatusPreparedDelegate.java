package com.foodordering.orderservice.workflow;

import com.foodordering.orderservice.event.DeliveryPendingEvent;
import com.foodordering.orderservice.model.Order;
import com.foodordering.orderservice.repository.OrderRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component("updateStatusPreparedDelegate")
public class UpdateStatusPreparedDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(UpdateStatusPreparedDelegate.class);

    private final OrderRepository orderRepository;
    private final JmsTemplate jmsTemplate;

    public UpdateStatusPreparedDelegate(OrderRepository orderRepository, JmsTemplate jmsTemplate) {
        this.orderRepository = orderRepository;
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long orderId = (Long) execution.getVariable("orderId");
        log.info("Executing UpdateStatusPreparedDelegate for order ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        order.setStatus("DELIVERY");
        orderRepository.save(order);
        log.info("Order ID {} status updated to DELIVERY", orderId);

        // Publish to delivery.pending queue
        DeliveryPendingEvent event = new DeliveryPendingEvent(
                order.getId(),
                order.getCustomerName(),
                order.getFoodItem()
        );

        log.info("Publishing DeliveryPendingEvent for order ID: {}", orderId);
        jmsTemplate.convertAndSend("delivery.pending", event);
    }
}
