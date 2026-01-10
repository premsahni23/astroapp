package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.request.MessageRequest;
import com.dekhokaun.mindarobackend.payload.response.MessageResponse;
import com.dekhokaun.mindarobackend.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message Controller", description = "REST APIs for managing chat messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(summary = "Send a message")
    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(@Valid @RequestBody MessageRequest request) {
        return ResponseEntity.ok(messageService.sendMessage(request));
    }

    @Operation(summary = "Get messages for a chat room")
    @GetMapping("/chatroom/{chatRoomId}")
    public ResponseEntity<List<MessageResponse>> getChatRoomMessages(@PathVariable String chatRoomId) {
        return ResponseEntity.ok(messageService.getChatRoomMessages(chatRoomId));
    }

    @Operation(summary = "Get paginated messages for a chat room")
    @GetMapping("/chatroom/{chatRoomId}/paginated")
    public ResponseEntity<List<MessageResponse>> getChatRoomMessagesPaginated(
            @PathVariable String chatRoomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(messageService.getChatRoomMessagesPaginated(chatRoomId, page, size));
    }

    @Operation(summary = "Get message count for a chat room")
    @GetMapping("/chatroom/{chatRoomId}/count")
    public ResponseEntity<Long> getMessageCount(@PathVariable String chatRoomId) {
        return ResponseEntity.ok(messageService.getMessageCount(chatRoomId));
    }
}