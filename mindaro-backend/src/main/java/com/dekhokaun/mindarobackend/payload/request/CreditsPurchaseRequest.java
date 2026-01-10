package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreditsPurchaseRequest {
    @NotNull
    private UUID userId;
    @NotNull
    private Integer credits;
    @NotNull
    private BigDecimal amount; // amount to deduct from wallet
    private String ref;
}
