package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class NotificationPreferenceRequest {
    @NotNull
    private UUID userId;
    private Boolean pushEnabled;
    private Boolean emailEnabled;
    private Boolean smsEnabled;
}
