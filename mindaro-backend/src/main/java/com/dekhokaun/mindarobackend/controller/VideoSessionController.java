package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.model.VideoSession;
import com.dekhokaun.mindarobackend.payload.request.VideoSessionCreateRequest;
import com.dekhokaun.mindarobackend.payload.request.VideoSessionEndRequest;
import com.dekhokaun.mindarobackend.payload.request.VideoSessionJoinRequest;
import com.dekhokaun.mindarobackend.service.VideoSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/video_sessions")
@Tag(name = "Video Sessions", description = "Video session APIs")
public class VideoSessionController {

    private final VideoSessionService videoSessionService;

    public VideoSessionController(VideoSessionService videoSessionService) {
        this.videoSessionService = videoSessionService;
    }

    @Operation(summary = "Create video session")
    @PostMapping("/create")
    public ResponseEntity<VideoSession> create(@Valid @RequestBody VideoSessionCreateRequest request) {
        return ResponseEntity.ok(videoSessionService.create(request));
    }

    @Operation(summary = "Join video session")
    @PostMapping("/join")
    public ResponseEntity<VideoSession> join(@Valid @RequestBody VideoSessionJoinRequest request) {
        return ResponseEntity.ok(videoSessionService.join(request.getRoomId()));
    }

    @Operation(summary = "End video session")
    @PostMapping("/end")
    public ResponseEntity<VideoSession> end(@Valid @RequestBody VideoSessionEndRequest request) {
        return ResponseEntity.ok(videoSessionService.end(request.getRoomId()));
    }

    @Operation(summary = "Get user video sessions")
    @GetMapping("/{userId}")
    public ResponseEntity<List<VideoSession>> byUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(videoSessionService.byUser(userId));
    }
}
