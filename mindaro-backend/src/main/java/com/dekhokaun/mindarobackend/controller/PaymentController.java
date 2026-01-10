package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.model.Payment;
import com.dekhokaun.mindarobackend.payload.request.PaymentCreateRequest;
import com.dekhokaun.mindarobackend.payload.request.PaymentRefundRequest;
import com.dekhokaun.mindarobackend.payload.request.PaymentVerifyRequest;
import com.dekhokaun.mindarobackend.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "Payment APIs")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Create payment")
    @PostMapping("/create")
    public ResponseEntity<Payment> create(@Valid @RequestBody PaymentCreateRequest request) {
        return ResponseEntity.ok(paymentService.create(request));
    }

    @Operation(summary = "Verify payment")
    @PostMapping("/verify")
    public ResponseEntity<Payment> verify(@Valid @RequestBody PaymentVerifyRequest request) {
        return ResponseEntity.ok(paymentService.verify(request));
    }

    @Operation(summary = "Get payment history")
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Payment>> history(@PathVariable UUID userId) {
        return ResponseEntity.ok(paymentService.history(userId));
    }

    @Operation(summary = "Refund payment")
    @PostMapping("/refund")
    public ResponseEntity<Payment> refund(@Valid @RequestBody PaymentRefundRequest request) {
        return ResponseEntity.ok(paymentService.refund(request));
    }
}
