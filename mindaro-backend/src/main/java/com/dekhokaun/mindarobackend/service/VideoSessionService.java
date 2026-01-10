package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.ResourceNotFoundException;
import com.dekhokaun.mindarobackend.model.Mentor;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.model.VideoSession;
import com.dekhokaun.mindarobackend.model.VideoSessionStatus;
import com.dekhokaun.mindarobackend.payload.request.VideoSessionCreateRequest;
import com.dekhokaun.mindarobackend.repository.MentorRepository;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import com.dekhokaun.mindarobackend.repository.VideoSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoSessionService {

    private final VideoSessionRepository videoSessionRepository;
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;

    public VideoSession create(VideoSessionCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Handle mentor lookup - extract numeric ID from UUID if it's a fake UUID
        Mentor mentor = null;
        try {
            // First try to find by UUID directly
            mentor = mentorRepository.findById(request.getMentorId())
                    .orElse(null);
        } catch (Exception e) {
            log.debug("Direct UUID lookup failed for mentor: {}", request.getMentorId());
        }
        
        // If UUID lookup failed, try to extract numeric ID from fake UUID pattern
        if (mentor == null) {
            try {
                String mentorIdStr = request.getMentorId().toString();
                // Check if it's a fake UUID pattern: 00000000-0000-4000-8000-{numeric_id}
                if (mentorIdStr.startsWith("00000000-0000-4000-8000-")) {
                    String numericPart = mentorIdStr.substring(24); // Extract the last 12 characters
                    int mentorUmid = Integer.parseInt(numericPart);
                    mentor = mentorRepository.findByUmid(mentorUmid).orElse(null);
                    log.debug("Extracted numeric ID {} from fake UUID", mentorUmid);
                }
            } catch (Exception ex) {
                log.debug("Failed to extract numeric ID from fake UUID: {}", request.getMentorId());
            }
        }
        
        if (mentor == null) {
            throw new ResourceNotFoundException("Mentor not found with ID: " + request.getMentorId());
        }

        String roomId = request.getRoomId();
        if (roomId == null || roomId.isBlank()) {
            roomId = user.getId() + "_" + mentor.getUmid();
        }

        VideoSession vs = videoSessionRepository.findByRoomId(roomId).orElseGet(VideoSession::new);
        vs.setRoomId(roomId);
        vs.setUser(user);
        
        // Create a simple mentor reference to avoid circular serialization
        Mentor simpleMentor = new Mentor();
        simpleMentor.setId(mentor.getId());
        simpleMentor.setUmid(mentor.getUmid());
        simpleMentor.setName(mentor.getName());
        simpleMentor.setEmail(mentor.getEmail());
        simpleMentor.setMobile(mentor.getMobile());
        simpleMentor.setRating(mentor.getRating());
        simpleMentor.setRatingCount(mentor.getRatingCount());
        // Don't set categories to avoid circular reference
        
        vs.setMentor(simpleMentor);
        vs.setStatus(VideoSessionStatus.CREATED);
        vs.setMeta(request.getMeta());
        return videoSessionRepository.save(vs);
    }

    public VideoSession join(String roomId) {
        VideoSession vs = videoSessionRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Video session not found"));
        vs.setStatus(VideoSessionStatus.JOINED);
        if (vs.getStartedAt() == null) {
            vs.setStartedAt(LocalDateTime.now());
        }
        return videoSessionRepository.save(vs);
    }

    public VideoSession end(String roomId) {
        VideoSession vs = videoSessionRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Video session not found"));
        vs.setStatus(VideoSessionStatus.ENDED);
        vs.setEndedAt(LocalDateTime.now());
        return videoSessionRepository.save(vs);
    }

    public List<VideoSession> byUser(UUID userId) {
        return videoSessionRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public VideoSession getById(UUID id) {
        return videoSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video session not found"));
    }
}
