package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.VideoSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VideoSessionRepository extends BaseRepository<VideoSession> {
    Optional<VideoSession> findByRoomId(String roomId);
    List<VideoSession> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<VideoSession> findByMentorIdOrderByCreatedAtDesc(UUID mentorId);
}
