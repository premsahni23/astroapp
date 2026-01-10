package com.dekhokaun.mindarobackend.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpSendRequest {

    @NotBlank
    private String mobileOrEmail;

    @NotBlank
    private String userid;

    @NotBlank
    private String countryCode;

    @NotBlank
    private String otpType; // email or mobile

    @Schema(hidden = true)
    private String ip;

}
