package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {
    List<ChatRoom> findByParticipants_Id(UUID userId);
}
