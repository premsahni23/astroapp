package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.request.EndSessionRequest;
import com.dekhokaun.mindarobackend.payload.request.StartSessionRequest;
import com.dekhokaun.mindarobackend.payload.response.SessionStatusResponse;
import com.dekhokaun.mindarobackend.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing")
@Tag(name = "Billing", description = "Session billing and wallet management for calls/chats")
@RequiredArgsConstructor
@Slf4j
public class BillingController {

    private final BillingService billingService;

    @Operation(summary = "Start billable session", 
               description = "Starts a new session (call/chat/video) and checks wallet balance")
    @PostMapping("/session/start")
    public ResponseEntity<SessionStatusResponse> startSession(@Valid @RequestBody StartSessionRequest request) {
        log.info("Starting session webhook: {}", request.getSessionId());
        
        try {
            SessionStatusResponse response = billingService.startSession(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error starting session: {}", request.getSessionId(), e);
            
            SessionStatusResponse errorResponse = SessionStatusResponse.builder()
                    .sessionId(request.getSessionId())
                    .status("ERROR")
                    .message(e.getMessage())
                    .build();
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "End billable session", 
               description = "Ends a session and calculates final billing")
    @PostMapping("/session/end")
    public ResponseEntity<SessionStatusResponse> endSession(@Valid @RequestBody EndSessionRequest request) {
        log.info("Ending session webhook: {}", request.getSessionId());
        
        try {
            SessionStatusResponse response = billingService.endSession(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error ending session: {}", request.getSessionId(), e);
            
            SessionStatusResponse errorResponse = SessionStatusResponse.builder()
                    .sessionId(request.getSessionId())
                    .status("ERROR")
                    .message(e.getMessage())
                    .build();
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Check session balance", 
               description = "Checks current wallet balance and estimated remaining time for active session")
    @GetMapping("/session/{sessionId}/status")
    public ResponseEntity<SessionStatusResponse> checkSessionStatus(@PathVariable String sessionId) {
        log.debug("Checking session status: {}", sessionId);
        
        try {
            SessionStatusResponse response = billingService.checkSessionBalance(sessionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error checking session status: {}", sessionId, e);
            
            SessionStatusResponse errorResponse = SessionStatusResponse.builder()
                    .sessionId(sessionId)
                    .status("ERROR")
                    .message(e.getMessage())
                    .build();
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Get billing rates", 
               description = "Returns current billing rates for different session types")
    @GetMapping("/rates")
    public ResponseEntity<?> getBillingRates() {
        return ResponseEntity.ok(new Object() {
            public final String audioCall = "₹11.00/minute";
            public final String videoCall = "₹17.00/minute"; // Updated rate
            public final String chat = "₹5.00/minute";
            public final String minimumBalance = "₹34.00 (2 minutes for video call)"; // Updated minimum
            public final String note = "Billing is done per minute with minimum 1 minute charge";
        });
    }
}