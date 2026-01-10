package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SliderRequest {

    @NotBlank
    private String imageUrl;

    private String title;
    private String description;
}
