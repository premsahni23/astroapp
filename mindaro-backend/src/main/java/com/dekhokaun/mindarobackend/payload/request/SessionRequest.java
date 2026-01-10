package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SessionRequest {

    @NotBlank
    private String userid;

    private String browser;

    private String ip;

    private String gps;

    private String logintype;
}
