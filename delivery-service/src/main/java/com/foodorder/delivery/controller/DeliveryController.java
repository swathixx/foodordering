package com.foodorder.delivery.controller;

import com.foodorder.delivery.event.DeliveryCompletedEvent;
import com.foodorder.delivery.model.Delivery;
import com.foodorder.delivery.model.DeliveryStatus;
import com.foodorder.delivery.repository.DeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private static final Logger log = LoggerFactory.getLogger(DeliveryController.class);

    private final DeliveryRepository deliveryRepository;
    private final JmsTemplate jmsTemplate;
    private final String deliveryCompletedQueue;

    public DeliveryController(DeliveryRepository deliveryRepository,
                              JmsTemplate jmsTemplate,
                              @Value("${delivery.completed.queue.name}") String deliveryCompletedQueue) {
        this.deliveryRepository = deliveryRepository;
        this.jmsTemplate = jmsTemplate;
        this.deliveryCompletedQueue = deliveryCompletedQueue;
    }

    @GetMapping
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    @PostMapping("/{id}/complete")
    public Delivery completeDelivery(@PathVariable Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Delivery not found"));

        if (delivery.getStatus() == DeliveryStatus.COMPLETED) {
            return delivery;
        }

        delivery.setStatus(DeliveryStatus.COMPLETED);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        log.info("Delivery ID {} (Order ID {}) marked as COMPLETED", id, savedDelivery.getOrderId());

        // Publish event
        DeliveryCompletedEvent event = new DeliveryCompletedEvent(savedDelivery.getOrderId(), "DELIVERED");
        log.info("Publishing DeliveryCompletedEvent to queue [{}]: {}", deliveryCompletedQueue, event);
        jmsTemplate.convertAndSend(deliveryCompletedQueue, event);

        return savedDelivery;
    }
}
