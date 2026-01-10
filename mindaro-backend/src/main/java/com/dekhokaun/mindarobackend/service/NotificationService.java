package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.payload.request.NotifyCallRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationService {

    public void sendNotification(NotifyCallRequest request) {
        try {
            String body = String.format("%s is trying to %s call you",
                    request.getUserName(),
                    request.getCallType().getValue());

            Notification notification = Notification.builder()
                    .setTitle("Incoming Call")
                    .setBody(body)
                    .build();

            final Map<String, Object> payload = buildCallPayload(request);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(payload);

            Map<String, String> data = new HashMap<>();
            data.put("type", "call");
            data.put("payload", json);

            Message message = Message.builder()
                    .setToken(getUserTokenById(request.getUserId()))
                    .setNotification(notification)
                    .putAllData(data)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Successfully sent notification: {}", response);

        } catch (FirebaseMessagingException e) {
            log.error("Failed to send notification", e);
            throw new RuntimeException("Failed to send notification", e);
        } catch (Exception e) {
            log.error("Unexpected error during notification send", e);
            throw new RuntimeException("Unexpected error during notification send", e);
        }
    }

    public void sendMulticastNotification(List<String> tokens, String title, String body, Map<String, String> data) {
        try {
            MulticastMessage message = MulticastMessage.builder()
                    .addAllTokens(tokens)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putAllData(data)
                    .build();

            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
            log.info("Successfully sent multicast notification: {} successful, {} failed",
                    response.getSuccessCount(), response.getFailureCount());
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send multicast notification", e);
            throw new RuntimeException("Failed to send multicast notification", e);
        }
    }

    public void sendTopicNotification(String topic, String title, String body, Map<String, String> data) {
        try {
            Message message = Message.builder()
                    .setTopic(topic)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putAllData(data)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Successfully sent topic notification: {}", response);
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send topic notification", e);
            throw new RuntimeException("Failed to send topic notification", e);
        }
    }

    public String getUserTokenById(String userId) {
        Firestore db = FirestoreClient.getFirestore();
        try {
            ApiFuture<DocumentSnapshot> future = db.collection("users").document(userId).get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.getString("user_token");
            } else {
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching user token: " + e.getMessage());
            return null;
        }
    }

    public Map<String, Object> buildCallPayload(NotifyCallRequest request) {
        NotifyCallRequest.CallType callType = request.getCallType();
        Map<String, String> data = new HashMap<>();
        data.put("type", "call");
        data.put("callType", callType.getValue());
        data.put("title", "Incoming Call");
        data.put("body", String.format("%s is trying to %s call you", request.getUserName(),
                "video".equalsIgnoreCase(callType.getValue()) ? "video" : "voice"));
        data.put("roomId", request.getRoomId());
        data.put("creatorId", request.getCreatorId());
        data.put("calleeId", request.getUserId());

        Map<String, Object> payload = new HashMap<>();
        payload.put("token", getUserTokenById(request.getUserId()));
        payload.put("data", data);

        return payload;
    }
}