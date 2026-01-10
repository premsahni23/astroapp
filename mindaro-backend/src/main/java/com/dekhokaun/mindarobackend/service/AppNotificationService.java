package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.ResourceNotFoundException;
import com.dekhokaun.mindarobackend.model.AppNotification;
import com.dekhokaun.mindarobackend.model.NotificationPreference;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.payload.request.NotificationPreferenceRequest;
import com.dekhokaun.mindarobackend.payload.request.NotificationScheduleRequest;
import com.dekhokaun.mindarobackend.repository.AppNotificationRepository;
import com.dekhokaun.mindarobackend.repository.NotificationPreferenceRepository;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppNotificationService {

    private final AppNotificationRepository appNotificationRepository;
    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final UserRepository userRepository;

    public AppNotification schedule(NotificationScheduleRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        AppNotification n = new AppNotification();
        n.setUser(user);
        n.setTitle(request.getTitle());
        n.setBody(request.getBody());
        n.setDataJson(request.getDataJson());
        n.setScheduledAt(request.getScheduledAt());
        // if scheduledAt is null, treat as immediate
        if (request.getScheduledAt() == null) {
            n.setSentAt(LocalDateTime.now());
        }
        return appNotificationRepository.save(n);
    }

    public List<AppNotification> list(UUID userId) {
        return appNotificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public AppNotification markRead(UUID notificationId) {
        AppNotification n = appNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        n.setIsRead(true);
        return appNotificationRepository.save(n);
    }

    public NotificationPreference setPreferences(NotificationPreferenceRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        NotificationPreference pref = notificationPreferenceRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    NotificationPreference p = new NotificationPreference();
                    p.setUser(user);
                    return p;
                });

        if (request.getPushEnabled() != null) pref.setPushEnabled(request.getPushEnabled());
        if (request.getEmailEnabled() != null) pref.setEmailEnabled(request.getEmailEnabled());
        if (request.getSmsEnabled() != null) pref.setSmsEnabled(request.getSmsEnabled());
        return notificationPreferenceRepository.save(pref);
    }

    public NotificationPreference getPreferences(UUID userId) {
        return notificationPreferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification preferences not found"));
    }
}
