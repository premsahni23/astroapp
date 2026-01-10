package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PaymentRefundRequest {
    @NotNull
    private UUID paymentId;
    private String reason;
}
