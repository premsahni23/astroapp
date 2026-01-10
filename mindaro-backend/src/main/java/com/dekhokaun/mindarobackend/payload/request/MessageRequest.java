package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageRequest {
    
    @NotBlank(message = "Content is required")
    private String content;
    
    @NotBlank(message = "Sender ID is required")
    private String senderId;
    
    @NotBlank(message = "Chat room ID is required")
    private String chatRoomId;
    
    @NotNull(message = "Message type is required")
    private MessageType messageType = MessageType.TEXT;
    
    public enum MessageType {
        TEXT, IMAGE, FILE, SYSTEM
    }
}