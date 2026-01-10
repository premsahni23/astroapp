package com.dekhokaun.mindarobackend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private String id;
    private String content;
    private String senderId;
    private String senderName;
    private String chatRoomId;
    private String messageType;
    private LocalDateTime createdAt;
}