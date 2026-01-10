package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.model.AnalyticsEvent;
import com.dekhokaun.mindarobackend.payload.request.AnalyticsEventRequest;
import com.dekhokaun.mindarobackend.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics", description = "Analytics APIs")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Operation(summary = "Record analytics event")
    @PostMapping("/event")
    public ResponseEntity<AnalyticsEvent> event(@Valid @RequestBody AnalyticsEventRequest request) {
        return ResponseEntity.ok(analyticsService.record(request));
    }

    @Operation(summary = "Get analytics dashboard")
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard() {
        return ResponseEntity.ok(analyticsService.dashboard());
    }
}
