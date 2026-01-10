package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class VideoSessionCreateRequest {
    @NotNull
    private UUID userId;
    @NotNull
    private UUID mentorId;
    // if null, server generates
    private String roomId;
    private String meta;
}
