package com.dekhokaun.mindarobackend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private String mobile;
    private String country;
    private String userType;
    private boolean isProfileCompleted;
}
