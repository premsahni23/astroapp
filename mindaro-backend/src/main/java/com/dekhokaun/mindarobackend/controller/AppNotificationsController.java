package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.model.AppNotification;
import com.dekhokaun.mindarobackend.model.NotificationPreference;
import com.dekhokaun.mindarobackend.payload.request.NotificationPreferenceRequest;
import com.dekhokaun.mindarobackend.payload.request.NotificationScheduleRequest;
import com.dekhokaun.mindarobackend.service.AppNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "In-app notification APIs")
public class AppNotificationsController {

    private final AppNotificationService appNotificationService;

    public AppNotificationsController(AppNotificationService appNotificationService) {
        this.appNotificationService = appNotificationService;
    }

    @Operation(summary = "Schedule notification")
    @PostMapping("/schedule")
    public ResponseEntity<AppNotification> schedule(@Valid @RequestBody NotificationScheduleRequest request) {
        return ResponseEntity.ok(appNotificationService.schedule(request));
    }

    @Operation(summary = "Get notifications")
    @GetMapping("/{userId}")
    public ResponseEntity<List<AppNotification>> list(@PathVariable UUID userId) {
        return ResponseEntity.ok(appNotificationService.list(userId));
    }

    @Operation(summary = "Get notifications (alias)")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppNotification>> listAlias(@PathVariable UUID userId) {
        return ResponseEntity.ok(appNotificationService.list(userId));
    }

    @Operation(summary = "Mark notification as read")
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<AppNotification> read(@PathVariable UUID notificationId) {
        return ResponseEntity.ok(appNotificationService.markRead(notificationId));
    }

    @Operation(summary = "Set notification preferences")
    @PostMapping("/preferences")
    public ResponseEntity<NotificationPreference> preferences(@Valid @RequestBody NotificationPreferenceRequest request) {
        return ResponseEntity.ok(appNotificationService.setPreferences(request));
    }

    @Operation(summary = "Get notification preferences")
    @GetMapping("/preferences/{userId}")
    public ResponseEntity<NotificationPreference> getPreferences(@PathVariable UUID userId) {
        return ResponseEntity.ok(appNotificationService.getPreferences(userId));
    }
}
