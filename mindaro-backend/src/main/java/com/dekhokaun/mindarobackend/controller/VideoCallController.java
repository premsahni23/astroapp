package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.model.VideoSession;
import com.dekhokaun.mindarobackend.payload.request.VideoSessionCreateRequest;
import com.dekhokaun.mindarobackend.payload.request.VideoSessionEndRequest;
import com.dekhokaun.mindarobackend.payload.request.VideoSessionJoinRequest;
import com.dekhokaun.mindarobackend.service.VideoSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Compatibility controller to match the mobile-app endpoint contract:
 *  - /api/video/session/create
 *  - /api/video/session/{id}
 *  - /api/video/session/join
 *  - /api/video/session/end
 */
@RestController
@RequestMapping("/api/video/session")
@Tag(name = "Video Call", description = "Video call session APIs (contract-compatible)")
@RequiredArgsConstructor
public class VideoCallController {

    private final VideoSessionService videoSessionService;

    @Operation(summary = "Create video session")
    @PostMapping("/create")
    public ResponseEntity<VideoSession> create(@Valid @RequestBody VideoSessionCreateRequest request) {
        return ResponseEntity.ok(videoSessionService.create(request));
    }

    @Operation(summary = "Get session details")
    @GetMapping("/{id}")
    public ResponseEntity<VideoSession> get(@PathVariable UUID id) {
        return ResponseEntity.ok(videoSessionService.getById(id));
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
}
