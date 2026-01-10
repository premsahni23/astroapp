package com.dekhokaun.mindarobackend.payload.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpVerifyRequest {

    @NotBlank
    private String mobileOrEmail;

    @NotBlank
    private String userid;

    @NotBlank
    private String countryCode;

    @NotBlank
    private String otpType;

    @NotBlank
    private String otp;
}