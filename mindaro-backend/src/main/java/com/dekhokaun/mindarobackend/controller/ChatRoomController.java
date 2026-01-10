package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.request.ChatRoomRequest;
import com.dekhokaun.mindarobackend.payload.response.ChatRoomResponse;
import com.dekhokaun.mindarobackend.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
@Tag(name = "Chat Room Controller", description = "Socket-style APIs for managing chat rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @Operation(summary = "Create chat room")
    @PostMapping
    public ResponseEntity<ChatRoomResponse> createChatRoom(@Valid @RequestBody ChatRoomRequest request) {
        return ResponseEntity.ok(chatRoomService.createChatRoom(request));
    }

    @Operation(summary = "Join chat room")
    @PostMapping("/{chatRoomId}/join/{userId}")
    public ResponseEntity<ChatRoomResponse> joinChatRoom(@PathVariable String chatRoomId, @PathVariable String userId) {
        return ResponseEntity.ok(chatRoomService.joinChatRoom(chatRoomId, userId));
    }

    @Operation(summary = "Leave chat room")
    @PostMapping("/{chatRoomId}/leave/{userId}")
    public ResponseEntity<ChatRoomResponse> leaveChatRoom(@PathVariable String chatRoomId, @PathVariable String userId) {
        return ResponseEntity.ok(chatRoomService.leaveChatRoom(chatRoomId, userId));
    }

    @Operation(summary = "List chat rooms for user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatRoomResponse>> listChatRoomsForUser(@PathVariable String userId) {
        return ResponseEntity.ok(chatRoomService.listChatRoomsForUser(userId));
    }

    @Operation(summary = "List all chat rooms")
    @GetMapping
    public ResponseEntity<List<ChatRoomResponse>> listChatRooms() {
        return ResponseEntity.ok(chatRoomService.listChatRooms());
    }

    @Operation(summary = "Get chat room details")
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ChatRoomResponse> getChatRoom(@PathVariable String chatRoomId) {
        return ResponseEntity.ok(chatRoomService.getChatRoomById(chatRoomId));
    }
}
