package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.AppNotification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppNotificationRepository extends BaseRepository<AppNotification> {
    List<AppNotification> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
