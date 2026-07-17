package com.foodorder.payment.service;

import com.foodorder.payment.dto.PaymentRequestDto;
import com.foodorder.payment.dto.PaymentResponseDto;

import java.util.List;

public interface PaymentService {
    PaymentResponseDto createPayment(PaymentRequestDto request);
    List<PaymentResponseDto> getAllPayments();
    PaymentResponseDto getPaymentById(Long id);
    PaymentResponseDto updatePayment(Long id, PaymentRequestDto request);
    void deletePayment(Long id);
}
