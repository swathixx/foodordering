package com.foodorder.kitchen.controller;

import com.foodorder.kitchen.event.KitchenCompletedEvent;
import com.foodorder.kitchen.model.KitchenOrder;
import com.foodorder.kitchen.model.KitchenStatus;
import com.foodorder.kitchen.repository.KitchenOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/kitchen")
@CrossOrigin(origins = "http://localhost:3000")
public class KitchenController {

    private static final Logger log = LoggerFactory.getLogger(KitchenController.class);

    private final KitchenOrderRepository kitchenOrderRepository;
    private final JmsTemplate jmsTemplate;
    private final String kitchenCompletedQueue;

    public KitchenController(KitchenOrderRepository kitchenOrderRepository,
                             JmsTemplate jmsTemplate,
                             @Value("${kitchen.completed.queue.name}") String kitchenCompletedQueue) {
        this.kitchenOrderRepository = kitchenOrderRepository;
        this.jmsTemplate = jmsTemplate;
        this.kitchenCompletedQueue = kitchenCompletedQueue;
    }

    @GetMapping
    public List<KitchenOrder> getAllOrders() {
        return kitchenOrderRepository.findAll();
    }

    @PostMapping("/{id}/complete")
    public KitchenOrder completeOrder(@PathVariable Long id) {
        KitchenOrder kitchenOrder = kitchenOrderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kitchen order not found"));

        if (kitchenOrder.getStatus() == KitchenStatus.COMPLETED) {
            return kitchenOrder;
        }

        kitchenOrder.setStatus(KitchenStatus.COMPLETED);
        KitchenOrder savedOrder = kitchenOrderRepository.save(kitchenOrder);
        log.info("Kitchen order ID {} (Order ID {}) marked as COMPLETED", id, savedOrder.getOrderId());

        // Publish event
        KitchenCompletedEvent event = new KitchenCompletedEvent(savedOrder.getOrderId(), "COMPLETED");
        log.info("Publishing KitchenCompletedEvent to queue [{}]: {}", kitchenCompletedQueue, event);
        jmsTemplate.convertAndSend(kitchenCompletedQueue, event);

        return savedOrder;
    }
}
