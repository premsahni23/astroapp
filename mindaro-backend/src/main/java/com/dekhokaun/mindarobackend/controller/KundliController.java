package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.model.Kundli;
import com.dekhokaun.mindarobackend.payload.request.KundliAnalysisRequest;
import com.dekhokaun.mindarobackend.payload.request.KundliGenerateRequest;
import com.dekhokaun.mindarobackend.payload.request.KundliMatchRequest;
import com.dekhokaun.mindarobackend.service.KundliService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/kundli")
@Tag(name = "Kundli", description = "Kundli APIs")
public class KundliController {

    private final KundliService kundliService;

    public KundliController(KundliService kundliService) {
        this.kundliService = kundliService;
    }

    @Operation(summary = "Generate kundli")
    @PostMapping("/generate")
    public ResponseEntity<Kundli> generate(@Valid @RequestBody KundliGenerateRequest request) {
        return ResponseEntity.ok(kundliService.generate(request));
    }

    @Operation(summary = "Kundli match")
    @PostMapping("/match")
    public ResponseEntity<Map<String, Object>> match(@Valid @RequestBody KundliMatchRequest request) {
        return ResponseEntity.ok(kundliService.match(request));
    }

    @Operation(summary = "Kundli analysis")
    @PostMapping("/analysis")
    public ResponseEntity<Map<String, Object>> analysis(@Valid @RequestBody KundliAnalysisRequest request) {
        return ResponseEntity.ok(kundliService.analysis(request));
    }

    @Operation(summary = "Get user kundlis")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Kundli>> byUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(kundliService.byUser(userId));
    }
}
