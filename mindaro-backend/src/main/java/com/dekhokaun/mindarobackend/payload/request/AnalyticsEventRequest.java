package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class AnalyticsEventRequest {
    private UUID userId;
    @NotBlank
    private String eventName;
    private String propertiesJson;
    private String source;
}
