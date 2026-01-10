package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EndSessionRequest {
    
    @NotBlank(message = "Session ID is required")
    private String sessionId;
    
    private String reason; // NORMAL_END, INSUFFICIENT_BALANCE, USER_DISCONNECTED, etc.
    
    private String metadata; // Optional additional data
}