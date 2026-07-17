package com.foodordering.orderservice.service.impl;

import com.foodordering.orderservice.event.OrderCreatedEvent;
import com.foodordering.orderservice.exception.OrderNotFoundException;
import com.foodordering.orderservice.model.Order;
import com.foodordering.orderservice.publisher.OrderEventPublisher;
import com.foodordering.orderservice.repository.OrderRepository;
import com.foodordering.orderservice.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;
    private final org.camunda.bpm.engine.RuntimeService runtimeService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderEventPublisher orderEventPublisher,
                            org.camunda.bpm.engine.RuntimeService runtimeService) {
        this.orderRepository = orderRepository;
        this.orderEventPublisher = orderEventPublisher;
        this.runtimeService = runtimeService;
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        order.setId(null);
        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getCustomerName(),
                savedOrder.getFoodItem(),
                savedOrder.getAmount(),
                savedOrder.getStatus(),
                savedOrder.getCreatedAt()
        );

        orderEventPublisher.publishOrderCreatedEvent(event);

        return savedOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, Order order) {
        Order existingOrder = getOrderById(id);
        existingOrder.setCustomerName(order.getCustomerName());
        existingOrder.setFoodItem(order.getFoodItem());
        existingOrder.setAmount(order.getAmount());
        return orderRepository.save(existingOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order existingOrder = getOrderById(id);
        orderRepository.delete(existingOrder);
    }
}
