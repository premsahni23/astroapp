package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.model.Rating;
import com.dekhokaun.mindarobackend.payload.request.ReviewRequest;
import com.dekhokaun.mindarobackend.payload.request.ReviewUpdateRequest;
import com.dekhokaun.mindarobackend.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "Review APIs")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Add review")
    @PostMapping
    public ResponseEntity<Rating> add(@Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.create(request));
    }

    @Operation(summary = "Get mentor reviews")
    @GetMapping("/{mentorId}")
    public ResponseEntity<List<Rating>> byMentor(@PathVariable UUID mentorId) {
        return ResponseEntity.ok(reviewService.byMentor(mentorId));
    }

    @Operation(summary = "Update review")
    @PutMapping("/{reviewId}")
    public ResponseEntity<Rating> update(@PathVariable UUID reviewId,
                                         @Valid @RequestBody ReviewUpdateRequest request) {
        return ResponseEntity.ok(reviewService.update(reviewId, request));
    }

    @Operation(summary = "Delete review")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> delete(@PathVariable UUID reviewId) {
        reviewService.delete(reviewId);
        return ResponseEntity.noContent().build();
    }
}
