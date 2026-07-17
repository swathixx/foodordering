package com.foodordering.orderservice.controller;

import com.foodordering.orderservice.dto.OrderRequest;
import com.foodordering.orderservice.dto.OrderResponse;
import com.foodordering.orderservice.model.Order;
import com.foodordering.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody OrderRequest request) {
        Order order = mapToEntity(request);
        order.setStatus("PLACED");
        Order savedOrder = orderService.createOrder(order);
        return mapToResponse(savedOrder);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return mapToResponse(order);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequest request) {
        Order orderDetails = mapToEntity(request);
        Order updatedOrder = orderService.updateOrder(id, orderDetails);
        return mapToResponse(updatedOrder);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    private Order mapToEntity(OrderRequest request) {
        return Order.builder()
                .customerName(request.getCustomerName())
                .foodItem(request.getFoodItem())
                .amount(request.getAmount())
                .build();
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .foodItem(order.getFoodItem())
                .amount(order.getAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
