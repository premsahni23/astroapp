package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.model.Mentor;
import com.dekhokaun.mindarobackend.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@Tag(name = "Recommendations", description = "Recommendation APIs")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Operation(summary = "Get mentor recommendations")
    @GetMapping
    public ResponseEntity<List<Mentor>> recommend(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(recommendationService.recommendMentors(limit));
    }
}
