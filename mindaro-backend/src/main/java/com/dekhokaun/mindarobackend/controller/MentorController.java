package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.request.MentorRequest;
import com.dekhokaun.mindarobackend.payload.request.MentorUpdateRequest;
import com.dekhokaun.mindarobackend.payload.response.MentorResponse;
import com.dekhokaun.mindarobackend.service.MentorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/mentor")
@Tag(name = "Mentor Controller", description = "APIs for managing mentors")
public class MentorController {

    private final MentorService mentorService;

    public MentorController(MentorService mentorService) {
        this.mentorService = mentorService;
    }

    @Operation(summary = "Create mentor", description = "Create a new mentor profile (Admin only)")
    @PostMapping
    public ResponseEntity<MentorResponse> addMentor(@Valid @RequestBody MentorRequest request) {
        return ResponseEntity.ok(mentorService.addMentor(request));
    }

    @Operation(summary = "Get all mentors", description = "Retrieves a list of all mentors")
    @GetMapping("/list")
    public ResponseEntity<List<MentorResponse>> getMentors() {
        return ResponseEntity.ok(mentorService.getMentors());
    }

    @Operation(summary = "Get mentor by id", description = "Retrieves mentor details by id")
    @GetMapping("/{id}")
    public ResponseEntity<MentorResponse> getMentor(@PathVariable Integer id) {
        return ResponseEntity.ok(mentorService.getMentorById(id));
    }

    @Operation(summary = "Get mentor by UUID", description = "Retrieves mentor details by UUID")
    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<MentorResponse> getMentorByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(mentorService.getMentorByUuid(uuid));
    }

    @Operation(summary = "Delete a mentor", description = "Deletes a mentor by their name")
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Void> deleteMentor(
            @Parameter(description = "Name of the mentor to delete", required = true) @PathVariable String name) {
        mentorService.deleteMentor(name);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a mentor", description = "Updates mentor details (Admin only)")
    @PutMapping("/{id}")
    public ResponseEntity<MentorResponse> updateMentor(@PathVariable Integer id, @RequestBody MentorUpdateRequest request) {
        return ResponseEntity.ok(mentorService.updateMentor(id, request));
    }
}
