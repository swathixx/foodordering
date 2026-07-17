package com.foodordering.orderservice.workflow;

import com.foodordering.orderservice.event.KitchenPendingEvent;
import com.foodordering.orderservice.model.Order;
import com.foodordering.orderservice.repository.OrderRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component("updateStatusPaidDelegate")
public class UpdateStatusPaidDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(UpdateStatusPaidDelegate.class);

    private final OrderRepository orderRepository;
    private final JmsTemplate jmsTemplate;

    public UpdateStatusPaidDelegate(OrderRepository orderRepository, JmsTemplate jmsTemplate) {
        this.orderRepository = orderRepository;
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long orderId = (Long) execution.getVariable("orderId");
        log.info("Executing UpdateStatusPaidDelegate for order ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        order.setStatus("KITCHEN_PREPARATION");
        orderRepository.save(order);
        log.info("Order ID {} status updated to KITCHEN_PREPARATION", orderId);

        // Publish to kitchen.pending queue
        KitchenPendingEvent event = new KitchenPendingEvent(
                order.getId(),
                order.getCustomerName(),
                order.getFoodItem()
        );
        
        log.info("Publishing KitchenPendingEvent for order ID: {}", orderId);
        jmsTemplate.convertAndSend("kitchen.pending", event);
    }
}
