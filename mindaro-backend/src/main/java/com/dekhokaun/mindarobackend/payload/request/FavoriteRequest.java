package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class FavoriteRequest {
    @NotNull
    private UUID userId;
    @NotNull
    private UUID mentorId;
}
