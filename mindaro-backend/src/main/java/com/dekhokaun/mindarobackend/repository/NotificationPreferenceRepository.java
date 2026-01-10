package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.NotificationPreference;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationPreferenceRepository extends BaseRepository<NotificationPreference> {
    Optional<NotificationPreference> findByUserId(UUID userId);
}
