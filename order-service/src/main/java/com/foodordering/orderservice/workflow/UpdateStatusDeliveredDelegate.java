package com.foodordering.orderservice.workflow;

import com.foodordering.orderservice.model.Order;
import com.foodordering.orderservice.repository.OrderRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("updateStatusDeliveredDelegate")
public class UpdateStatusDeliveredDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(UpdateStatusDeliveredDelegate.class);

    private final OrderRepository orderRepository;

    public UpdateStatusDeliveredDelegate(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long orderId = (Long) execution.getVariable("orderId");
        log.info("Executing UpdateStatusDeliveredDelegate for order ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        order.setStatus("DELIVERED");
        orderRepository.save(order);
        log.info("Order ID {} status updated to DELIVERED", orderId);
    }
}
