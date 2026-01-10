package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.InvalidRequestException;
import com.dekhokaun.mindarobackend.model.ChatRoom;
import com.dekhokaun.mindarobackend.model.Message;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.payload.request.MessageRequest;
import com.dekhokaun.mindarobackend.payload.response.MessageResponse;
import com.dekhokaun.mindarobackend.repository.ChatRoomRepository;
import com.dekhokaun.mindarobackend.repository.MessageRepository;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public MessageResponse sendMessage(MessageRequest request) {
        // Validate chat room exists
        ChatRoom chatRoom = chatRoomRepository.findById(UUID.fromString(request.getChatRoomId()))
                .orElseThrow(() -> new InvalidRequestException("Chat room not found with id: " + request.getChatRoomId()));

        // Validate user exists
        User sender = userRepository.findById(UUID.fromString(request.getSenderId()))
                .orElseThrow(() -> new InvalidRequestException("User not found with id: " + request.getSenderId()));

        // Create message
        Message message = new Message();
        message.setContent(request.getContent());
        message.setSenderId(sender.getId());
        message.setSenderName(sender.getName());
        message.setChatRoomId(chatRoom.getId());
        message.setMessageType(request.getMessageType());

        Message savedMessage = messageRepository.save(message);
        return mapToResponse(savedMessage);
    }

    public List<MessageResponse> getChatRoomMessages(String chatRoomId) {
        UUID roomId = UUID.fromString(chatRoomId);
        
        // Validate chat room exists
        if (!chatRoomRepository.existsById(roomId)) {
            throw new InvalidRequestException("Chat room not found with id: " + chatRoomId);
        }

        List<Message> messages = messageRepository.findByChatRoomIdOrderByCreatedAtAsc(roomId);
        return messages.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<MessageResponse> getChatRoomMessagesPaginated(String chatRoomId, int page, int size) {
        UUID roomId = UUID.fromString(chatRoomId);
        
        // Validate chat room exists
        if (!chatRoomRepository.existsById(roomId)) {
            throw new InvalidRequestException("Chat room not found with id: " + chatRoomId);
        }

        Pageable pageable = PageRequest.of(page, size);
        return messageRepository.findByChatRoomIdOrderByCreatedAtDesc(roomId, pageable)
                .getContent()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Long getMessageCount(String chatRoomId) {
        UUID roomId = UUID.fromString(chatRoomId);
        
        // Validate chat room exists
        if (!chatRoomRepository.existsById(roomId)) {
            throw new InvalidRequestException("Chat room not found with id: " + chatRoomId);
        }

        return messageRepository.countByChatRoomId(roomId);
    }

    private MessageResponse mapToResponse(Message message) {
        return new MessageResponse(
                message.getId().toString(),
                message.getContent(),
                message.getSenderId().toString(),
                message.getSenderName(),
                message.getChatRoomId().toString(),
                message.getMessageType().toString(),
                message.getCreatedAt()
        );
    }
}