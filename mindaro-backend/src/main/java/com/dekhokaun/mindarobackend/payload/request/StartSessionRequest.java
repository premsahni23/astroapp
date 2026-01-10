package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class StartSessionRequest {
    
    @NotBlank(message = "Session ID is required")
    private String sessionId;
    
    @NotNull(message = "User ID is required")
    private UUID userId;
    
    @NotNull(message = "Mentor ID is required")
    private UUID mentorId;
    
    @NotBlank(message = "Session type is required")
    private String sessionType; // AUDIO_CALL, VIDEO_CALL, CHAT
    
    private String metadata; // Optional additional data
}