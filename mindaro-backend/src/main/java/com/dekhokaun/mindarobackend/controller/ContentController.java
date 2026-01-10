package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.model.Banner;
import com.dekhokaun.mindarobackend.model.Faq;
import com.dekhokaun.mindarobackend.model.Feedback;
import com.dekhokaun.mindarobackend.model.Testimonial;
import com.dekhokaun.mindarobackend.payload.request.FeedbackRequest;
import com.dekhokaun.mindarobackend.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Content", description = "Content APIs")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @Operation(summary = "Get banners")
    @GetMapping("/banners")
    public ResponseEntity<List<Banner>> banners() {
        return ResponseEntity.ok(contentService.banners());
    }

    @Operation(summary = "Get testimonials")
    @GetMapping("/testimonials")
    public ResponseEntity<List<Testimonial>> testimonials() {
        return ResponseEntity.ok(contentService.testimonials());
    }

    @Operation(summary = "Get FAQs")
    @GetMapping("/faqs")
    public ResponseEntity<List<Faq>> faqs() {
        return ResponseEntity.ok(contentService.faqs());
    }

    @Operation(summary = "Submit feedback")
    @PostMapping("/feedback")
    public ResponseEntity<Feedback> feedback(@Valid @RequestBody FeedbackRequest request) {
        return ResponseEntity.ok(contentService.submitFeedback(request));
    }
}
