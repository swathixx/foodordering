package com.foodordering.orderservice.workflow;

import com.foodordering.orderservice.model.Order;
import com.foodordering.orderservice.repository.OrderRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("updateStatusFailedDelegate")
public class UpdateStatusFailedDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(UpdateStatusFailedDelegate.class);

    private final OrderRepository orderRepository;

    public UpdateStatusFailedDelegate(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long orderId = (Long) execution.getVariable("orderId");
        log.info("Executing UpdateStatusFailedDelegate for order ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        order.setStatus("FAILED");
        orderRepository.save(order);
        log.info("Order ID {} status updated to FAILED", orderId);
    }
}
