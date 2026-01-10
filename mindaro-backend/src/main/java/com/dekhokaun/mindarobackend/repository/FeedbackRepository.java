package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Feedback;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FeedbackRepository extends BaseRepository<Feedback> {
    List<Feedback> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
