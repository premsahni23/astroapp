package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatRoomRequest {
    @NotBlank
    private String name;
}
