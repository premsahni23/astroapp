package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAdminRequest {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String mobile;

    @NotBlank
    private String password;

    @NotBlank
    private String country;
} 