package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class KundliMatchRequest {
    @NotNull
    private UUID kundliAId;
    @NotNull
    private UUID kundliBId;
}
