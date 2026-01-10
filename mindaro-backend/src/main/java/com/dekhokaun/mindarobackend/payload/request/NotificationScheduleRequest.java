package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationScheduleRequest {
    @NotNull
    private UUID userId;
    @NotBlank
    private String title;
    private String body;
    private String dataJson;
    private LocalDateTime scheduledAt;
}
