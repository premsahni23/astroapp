package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {
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

    /** Optional: ADMIN | CUSTOMER | MENTOR (dashboard sends this as utype) */
    private String utype;

    /** Optional: ACTIVE | INACTIVE */
    private String status;

    /** Optional: dashboard field name */
    private Boolean is_profile_completed;
}