package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentCreateRequest {
    @NotNull
    private UUID userId;
    @NotNull
    private BigDecimal amount;
    private String currency; // INR
    private String provider; // razorpay/stripe/manual
}
