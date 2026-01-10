package com.dekhokaun.mindarobackend.model;

import com.dekhokaun.mindarobackend.payload.request.MessageRequest;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "x1_message")
@Data
@NoArgsConstructor
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "sender_id", nullable = false)
    private UUID senderId;
    
    @Column(name = "sender_name")
    private String senderName;
    
    @Column(name = "chat_room_id", nullable = false)
    private UUID chatRoomId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    private MessageRequest.MessageType messageType = MessageRequest.MessageType.TEXT;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    // Many messages belong to one chat room
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", insertable = false, updatable = false)
    private ChatRoom chatRoom;
    
    // Many messages belong to one user (sender)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", insertable = false, updatable = false)
    private User sender;
}