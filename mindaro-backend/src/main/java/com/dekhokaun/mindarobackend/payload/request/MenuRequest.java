package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MenuRequest {

    @NotBlank
    private String text;

    private String icon;
    private String action;
    private String link;
    private int order;
}
