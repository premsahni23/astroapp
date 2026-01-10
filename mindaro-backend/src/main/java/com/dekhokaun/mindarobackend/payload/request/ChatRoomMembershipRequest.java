package com.dekhokaun.mindarobackend.payload.request;

public class ChatRoomMembershipRequest {
    private String chatRoomId;
    private String userId;

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
