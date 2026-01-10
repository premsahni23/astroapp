package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.request.ChatRoomMembershipRequest;
import com.dekhokaun.mindarobackend.payload.request.ChatRoomRequest;
import com.dekhokaun.mindarobackend.payload.request.ChatRoomUserRequest;
import com.dekhokaun.mindarobackend.payload.response.ChatRoomResponse;
import com.dekhokaun.mindarobackend.service.ChatRoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@MessageMapping("/chatrooms")
@Tag(name = "Chat Room Socket Controller", description = "WebSocket endpoints for chat room operations")
public class ChatRoomSocketController {

    private final ChatRoomService chatRoomService;

    public ChatRoomSocketController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @MessageMapping("/create")
    @SendTo("/topic/chatrooms")
    public ChatRoomResponse createChatRoom(ChatRoomRequest request) {
        return chatRoomService.createChatRoom(request);
    }

    @MessageMapping("/join")
    @SendTo("/topic/chatrooms")
    public ChatRoomResponse joinChatRoom(ChatRoomMembershipRequest request) {
        return chatRoomService.joinChatRoom(request.getChatRoomId(), request.getUserId());
    }

    @MessageMapping("/leave")
    @SendTo("/topic/chatrooms")
    public ChatRoomResponse leaveChatRoom(ChatRoomMembershipRequest request) {
        return chatRoomService.leaveChatRoom(request.getChatRoomId(), request.getUserId());
    }

    @MessageMapping("/listForUser")
    @SendToUser("/queue/chatrooms")
    public List<ChatRoomResponse> listChatRoomsForUser(ChatRoomUserRequest request) {
        return chatRoomService.listChatRoomsForUser(request.getUserId());
    }

    @MessageMapping("/listAll")
    @SendToUser("/queue/chatrooms")
    public List<ChatRoomResponse> listChatRooms() {
        return chatRoomService.listChatRooms();
    }
}
