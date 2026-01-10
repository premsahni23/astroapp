package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String mobile;

    @NotBlank
    private String country;

    @NotBlank
    private String password;
    private String token; // google_token if method is google

    @NotBlank
    private String method;
}
