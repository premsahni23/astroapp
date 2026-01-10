package com.dekhokaun.mindarobackend.payload.request;

import lombok.Data;

@Data
public class AdminUpdateUserRequest {
    private String name;
    private String mobile;
    private String password;
    private String country;

    /** Optional: ADMIN | CUSTOMER | MENTOR */
    private String utype;

    /** Optional: ACTIVE | INACTIVE */
    private String status;

    /** Optional: dashboard field name */
    private Boolean is_profile_completed;
}