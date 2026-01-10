package com.dekhokaun.mindarobackend.payload.response;

import lombok.Data;

import java.util.List;

/**
 * Contract aligned with admin-dashboard Mentor type.
 */
@Data
public class MentorResponse {
    /**
     * Numeric mentor id used by the admin dashboard.
     * Mapped from Mentor.umid.
     */
    private Integer id;

    private String name;
    private String email;
    /** Dashboard expects a string mobile number */
    private String mobile;
    private String country;

    /** ACTIVE | INACTIVE */
    private String status;

    /** Short profile/summary */
    private String about;

    /** List of expertise/category names */
    private List<String> expertise;

    private Double rating;
    private Integer ratingCount;

    /** ISO string */
    private String createdAt;
}
