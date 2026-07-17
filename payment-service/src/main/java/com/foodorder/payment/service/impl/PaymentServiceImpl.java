package com.foodorder.payment.service.impl;

import com.foodorder.payment.dto.PaymentRequestDto;
import com.foodorder.payment.dto.PaymentResponseDto;
import com.foodorder.payment.exception.ResourceNotFoundException;
import com.foodorder.payment.model.Payment;
import com.foodorder.payment.repository.PaymentRepository;
import com.foodorder.payment.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto request) {
        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .customerName(request.getCustomerName())
                .foodItem(request.getFoodItem())
                .amount(request.getAmount())
                .status(request.getStatus())
                .build();
        Payment savedPayment = paymentRepository.save(payment);
        return mapToResponse(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));
        return mapToResponse(payment);
    }

    @Override
    @Transactional
    public PaymentResponseDto updatePayment(Long id, PaymentRequestDto request) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));
        existingPayment.setOrderId(request.getOrderId());
        existingPayment.setCustomerName(request.getCustomerName());
        existingPayment.setFoodItem(request.getFoodItem());
        existingPayment.setAmount(request.getAmount());
        existingPayment.setStatus(request.getStatus());
        Payment updatedPayment = paymentRepository.save(existingPayment);
        return mapToResponse(updatedPayment);
    }

    @Override
    @Transactional
    public void deletePayment(Long id) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));
        paymentRepository.delete(existingPayment);
    }

    private PaymentResponseDto mapToResponse(Payment payment) {
        return PaymentResponseDto.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .customerName(payment.getCustomerName())
                .foodItem(payment.getFoodItem())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
