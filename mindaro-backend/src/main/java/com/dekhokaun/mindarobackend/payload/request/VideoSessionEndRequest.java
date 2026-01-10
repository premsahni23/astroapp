package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VideoSessionEndRequest {
    @NotBlank
    private String roomId;
}
