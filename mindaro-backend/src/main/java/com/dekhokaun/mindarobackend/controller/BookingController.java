package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.model.Booking;
import com.dekhokaun.mindarobackend.payload.request.BookingRequest;
import com.dekhokaun.mindarobackend.payload.request.BookingStatusUpdateRequest;
import com.dekhokaun.mindarobackend.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Bookings", description = "Booking related APIs")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(summary = "Create a booking")
    @PostMapping
    public ResponseEntity<Booking> create(@Valid @RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.create(request));
    }

    @Operation(summary = "Get user bookings")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> byUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(bookingService.byUser(userId));
    }

    @Operation(summary = "Get astrologer/mentor bookings")
    @GetMapping("/astrologer/{id}")
    public ResponseEntity<List<Booking>> byAstrologer(@PathVariable("id") UUID mentorId) {
        return ResponseEntity.ok(bookingService.byMentor(mentorId));
    }

    @Operation(summary = "Update booking status")
    @PutMapping("/{bookingId}/status")
    public ResponseEntity<Booking> updateStatus(@PathVariable UUID bookingId,
                                                @Valid @RequestBody BookingStatusUpdateRequest request) {
        return ResponseEntity.ok(bookingService.updateStatus(bookingId, request.getStatus()));
    }

    @Operation(summary = "Cancel booking")
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> cancel(@PathVariable UUID bookingId) {
        bookingService.cancel(bookingId);
        return ResponseEntity.noContent().build();
    }
}
