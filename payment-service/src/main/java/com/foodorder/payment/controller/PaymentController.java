package com.foodorder.payment.controller;

import com.foodorder.payment.dto.PaymentRequestDto;
import com.foodorder.payment.dto.PaymentResponseDto;
import com.foodorder.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponseDto createPayment(@Valid @RequestBody PaymentRequestDto request) {
        return paymentService.createPayment(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PaymentResponseDto> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponseDto getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponseDto updatePayment(@PathVariable Long id, @Valid @RequestBody PaymentRequestDto request) {
        return paymentService.updatePayment(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
    }
}
