package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    
    List<Message> findByChatRoomIdOrderByCreatedAtAsc(UUID chatRoomId);
    
    Page<Message> findByChatRoomIdOrderByCreatedAtDesc(UUID chatRoomId, Pageable pageable);
    
    long countByChatRoomId(UUID chatRoomId);
    
    @Query("SELECT m FROM Message m WHERE m.chatRoomId = :chatRoomId ORDER BY m.createdAt ASC")
    List<Message> findMessagesByChatRoomId(@Param("chatRoomId") UUID chatRoomId);
}