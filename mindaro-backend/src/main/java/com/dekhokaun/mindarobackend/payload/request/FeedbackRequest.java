package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class FeedbackRequest {
    private UUID userId;
    @NotBlank
    private String subject;
    @NotBlank
    private String message;
}
