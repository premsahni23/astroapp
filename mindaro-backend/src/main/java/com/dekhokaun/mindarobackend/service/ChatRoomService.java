package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.InvalidRequestException;
import com.dekhokaun.mindarobackend.model.ChatRoom;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.payload.request.ChatRoomRequest;
import com.dekhokaun.mindarobackend.payload.response.ChatRoomResponse;
import com.dekhokaun.mindarobackend.repository.ChatRoomRepository;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ChatRoomResponse createChatRoom(ChatRoomRequest request) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(request.getName());
        return mapToResponse(chatRoomRepository.save(chatRoom));
    }

    public ChatRoomResponse joinChatRoom(String chatRoomId, String userId) {
        ChatRoom chatRoom = getChatRoomOrThrow(chatRoomId);
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new InvalidRequestException("User not found with id: " + userId));
        chatRoom.getParticipants().add(user);
        return mapToResponse(chatRoomRepository.save(chatRoom));
    }

    public ChatRoomResponse leaveChatRoom(String chatRoomId, String userId) {
        ChatRoom chatRoom = getChatRoomOrThrow(chatRoomId);
        chatRoom.getParticipants().removeIf(user -> user.getId().toString().equals(userId));
        return mapToResponse(chatRoomRepository.save(chatRoom));
    }

    public List<ChatRoomResponse> listChatRoomsForUser(String userId) {
        return chatRoomRepository.findByParticipants_Id(UUID.fromString(userId)).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ChatRoomResponse> listChatRooms() {
        return chatRoomRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ChatRoomResponse getChatRoomById(String chatRoomId) {
        return mapToResponse(getChatRoomOrThrow(chatRoomId));
    }

    private ChatRoom getChatRoomOrThrow(String chatRoomId) {
        return chatRoomRepository.findById(UUID.fromString(chatRoomId))
                .orElseThrow(() -> new InvalidRequestException("Chat room not found with id: " + chatRoomId));
    }

    private ChatRoomResponse mapToResponse(ChatRoom chatRoom) {
        Set<String> participantIds = chatRoom.getParticipants().stream()
                .map(user -> user.getId().toString())
                .collect(Collectors.toSet());
        return new ChatRoomResponse(chatRoom.getId().toString(), chatRoom.getName(), participantIds);
    }
}
