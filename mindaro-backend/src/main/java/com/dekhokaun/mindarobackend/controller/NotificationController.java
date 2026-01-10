package com.dekhokaun.mindarobackend.controller;


import com.dekhokaun.mindarobackend.payload.request.NotifyCallRequest;
import com.dekhokaun.mindarobackend.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@Valid @RequestBody NotifyCallRequest request) {
        notificationService.sendNotification(request);
        return ResponseEntity.ok("Notification sent successfully");
    }

    /* @PostMapping("/send-multicast")
    public ResponseEntity<String> sendMulticastNotification(
            @RequestBody MulticastNotificationRequest multicastRequest) {

        notificationService.sendMulticastNotification(
                multicastRequest.getTokens(),
                multicastRequest.getTitle(),
                multicastRequest.getBody(),
                multicastRequest.getData()
        );
        return ResponseEntity.ok("Multicast notification sent successfully");
    }

    @PostMapping("/send-topic")
    public ResponseEntity<String> sendTopicNotification(
            @RequestBody TopicNotificationRequest topicRequest) {

        notificationService.sendTopicNotification(
                topicRequest.getTopic(),
                topicRequest.getTitle(),
                topicRequest.getBody(),
                topicRequest.getData()
        );
        return ResponseEntity.ok("Topic notification sent successfully");
    } */
}
