package com.foodordering.orderservice.service;

import com.foodordering.orderservice.event.OrderCreatedEvent;
import com.foodordering.orderservice.model.Order;
import com.foodordering.orderservice.publisher.OrderEventPublisher;
import com.foodordering.orderservice.repository.OrderRepository;
import com.foodordering.orderservice.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventPublisher orderEventPublisher;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder_PublishesEvent() {
        // Arrange
        Order orderToCreate = Order.builder()
                .customerName("Alice")
                .foodItem("Sushi")
                .amount(new BigDecimal("29.99"))
                .status("PENDING")
                .build();

        Order savedOrder = Order.builder()
                .id(1L)
                .customerName("Alice")
                .foodItem("Sushi")
                .amount(new BigDecimal("29.99"))
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        Order result = orderService.createOrder(orderToCreate);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Alice", result.getCustomerName());
        assertEquals("Sushi", result.getFoodItem());
        assertEquals(new BigDecimal("29.99"), result.getAmount());
        assertEquals("PENDING", result.getStatus());

        verify(orderRepository, times(1)).save(orderToCreate);
        verify(orderEventPublisher, times(1)).publishOrderCreatedEvent(any(OrderCreatedEvent.class));
    }

    @Test
    public void testUpdateOrder_PreservesStatus() {
        // Arrange
        Long orderId = 1L;
        Order existingOrder = Order.builder()
                .id(orderId)
                .customerName("John Doe")
                .foodItem("Burger")
                .amount(new BigDecimal("15.99"))
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        Order updateRequest = Order.builder()
                .customerName("Jane Doe")
                .foodItem("Pizza")
                .amount(new BigDecimal("25.99"))
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = orderService.updateOrder(orderId, updateRequest);

        // Assert
        assertEquals("Jane Doe", result.getCustomerName());
        assertEquals("Pizza", result.getFoodItem());
        assertEquals(new BigDecimal("25.99"), result.getAmount());
        assertEquals("PENDING", result.getStatus()); // Status must be preserved as PENDING
        
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(existingOrder);
    }
}
