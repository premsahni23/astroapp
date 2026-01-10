package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class KundliGenerateRequest {
    @NotNull
    private UUID userId;
    @NotBlank
    private String fullName;
    @NotBlank
    private String dob;
    @NotBlank
    private String tob;
    @NotBlank
    private String place;
}
