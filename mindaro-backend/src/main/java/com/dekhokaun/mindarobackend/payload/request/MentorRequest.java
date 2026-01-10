package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MentorRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private Long mobile;

    @NotBlank
    private String country;

    @NotBlank
    private String password;

    @NotBlank
    private String category;

    @Positive
    private Integer experience;

    // Optional fields
    private String firstName;
    private String lastName;
    private String nationality;
    private String photo;
    private String gender;
    private LocalDate dob;
    private String mainLanguage;
    private String address;
    private String city;
    private String state;
    private Integer pincode;
    private Integer timeWeeklyHrs;
    private String platformInfo;
    private String qualification;
    private String mainIncome;
    private String twitter;
    private String linkedin;
    private String youtube;
    private String facebook;
    private Integer rate;
}
