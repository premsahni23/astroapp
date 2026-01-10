package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WalletAddMoneyRequest {
    @NotNull
    private UUID userId;
    @NotNull
    private BigDecimal amount;
    private String method; // payment/wallet...
}
