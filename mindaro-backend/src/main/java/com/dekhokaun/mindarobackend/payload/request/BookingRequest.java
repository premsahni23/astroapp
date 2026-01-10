package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookingRequest {
    @NotNull
    private UUID userId;
    @NotNull
    private UUID mentorId;
    @NotNull
    private LocalDateTime scheduledAt;
    private String sessionType;
    private BigDecimal amount;
    private String notes;
}
