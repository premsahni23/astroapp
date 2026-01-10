package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Favorite;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteRepository extends BaseRepository<Favorite> {
    List<Favorite> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<Favorite> findByUserIdAndMentorId(UUID userId, UUID mentorId);
}
