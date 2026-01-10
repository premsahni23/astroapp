package com.dekhokaun.mindarobackend.payload.request;

import lombok.Data;

import java.util.List;

/**
 * Update request aligned with admin-dashboard which sends partial fields.
 * All fields are optional.
 */
@Data
public class MentorUpdateRequest {
    private String name;
    private String email;
    private String mobile;
    private String country;
    /** ACTIVE | INACTIVE */
    private String status;
    private String about;
    private List<String> expertise;
    private Double rating;
    private Integer ratingCount;
}
