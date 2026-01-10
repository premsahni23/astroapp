package com.dekhokaun.mindarobackend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO that matches admin-dashboard User type.
 */
@Data
@AllArgsConstructor
public class AdminUserResponse {
    private long id;
    private String name;
    private String email;
    private String mobile;
    private String country;
    private String utype; // ADMIN | CUSTOMER | MENTOR
    private String status; // ACTIVE | INACTIVE
    private boolean is_profile_completed;
    private double rating;
    private int ratingCount;
    private String createdAt; // ISO string
}
