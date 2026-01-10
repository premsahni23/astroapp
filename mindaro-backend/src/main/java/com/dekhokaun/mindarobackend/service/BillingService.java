package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.InsufficientBalanceException;
import com.dekhokaun.mindarobackend.exception.ResourceNotFoundException;
import com.dekhokaun.mindarobackend.model.*;
import com.dekhokaun.mindarobackend.payload.request.StartSessionRequest;
import com.dekhokaun.mindarobackend.payload.request.EndSessionRequest;
import com.dekhokaun.mindarobackend.payload.response.SessionStatusResponse;
import com.dekhokaun.mindarobackend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;
    private final VideoSessionRepository videoSessionRepository;
    
    // In-memory session tracking for active billing
    private final ConcurrentHashMap<String, ActiveSession> activeSessions = new ConcurrentHashMap<>();
    
    // Pricing per minute (in rupees)
    private static final BigDecimal CALL_RATE_PER_MINUTE = new BigDecimal("11.00");
    private static final BigDecimal CHAT_RATE_PER_MINUTE = new BigDecimal("5.00");
    private static final BigDecimal VIDEO_CALL_RATE_PER_MINUTE = new BigDecimal("17.00"); // Updated to 17/min
    
    // Minimum balance required to start session (2 minutes worth)
    private static final BigDecimal MIN_BALANCE_MULTIPLIER = new BigDecimal("2");

    @Transactional
    public SessionStatusResponse startSession(StartSessionRequest request) {
        log.info("Starting session: {} for user: {}", request.getSessionId(), request.getUserId());
        
        // Get user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // For now, we'll store mentor ID directly in the session
        // In a real system, you'd validate the mentor exists
        log.info("Starting session for user: {} with mentor: {}", user.getEmail(), request.getMentorId());
        
        // Get or create wallet
        Wallet wallet = walletRepository.findByUser(user)
                .orElseGet(() -> {
                    log.info("Creating new wallet for user: {}", user.getEmail());
                    Wallet newWallet = new Wallet();
                    newWallet.setUser(user);
                    newWallet.setBalance(new BigDecimal("50.00")); // Initial balance
                    return walletRepository.save(newWallet);
                });
        
        // Determine rate based on session type
        BigDecimal ratePerMinute = getRateForSessionType(request.getSessionType());
        BigDecimal minRequiredBalance = ratePerMinute.multiply(MIN_BALANCE_MULTIPLIER);
        
        // Check if user has sufficient balance
        if (wallet.getBalance().compareTo(minRequiredBalance) < 0) {
            throw new InsufficientBalanceException(
                String.format("Insufficient balance. Required: ₹%.2f, Available: ₹%.2f", 
                    minRequiredBalance, wallet.getBalance())
            );
        }
        
        // Create video session record (skip for now to avoid mentor constraint)
        // VideoSession session = new VideoSession();
        // session.setRoomId(request.getSessionId());
        // session.setUser(user);
        // session.setStatus(VideoSessionStatus.CREATED);
        // session.setStartedAt(LocalDateTime.now());
        // videoSessionRepository.save(session);
        
        // Track active session for billing
        ActiveSession activeSession = new ActiveSession(
            request.getSessionId(),
            request.getUserId(),
            request.getMentorId(),
            request.getSessionType(),
            ratePerMinute,
            LocalDateTime.now(),
            wallet.getBalance()
        );
        activeSessions.put(request.getSessionId(), activeSession);
        
        log.info("Session started successfully: {}", request.getSessionId());
        
        return SessionStatusResponse.builder()
                .sessionId(request.getSessionId())
                .status("STARTED")
                .currentBalance(wallet.getBalance())
                .ratePerMinute(ratePerMinute)
                .estimatedMinutes(wallet.getBalance().divide(ratePerMinute, 0, BigDecimal.ROUND_DOWN).intValue())
                .message("Session started successfully")
                .build();
    }
    
    @Transactional
    public SessionStatusResponse endSession(EndSessionRequest request) {
        log.info("Ending session: {}", request.getSessionId());
        
        ActiveSession activeSession = activeSessions.remove(request.getSessionId());
        if (activeSession == null) {
            log.warn("Session not found in active sessions: {}", request.getSessionId());
            return SessionStatusResponse.builder()
                    .sessionId(request.getSessionId())
                    .status("NOT_FOUND")
                    .message("Session not found")
                    .build();
        }
        
        // Calculate total duration and cost
        LocalDateTime endTime = LocalDateTime.now();
        long totalMinutes = ChronoUnit.MINUTES.between(activeSession.getStartTime(), endTime);
        if (totalMinutes < 1) totalMinutes = 1; // Minimum 1 minute billing
        
        BigDecimal totalCost = activeSession.getRatePerMinute().multiply(new BigDecimal(totalMinutes));
        
        // Get current wallet
        User user = userRepository.findById(activeSession.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        
        // Deduct final amount
        try {
            wallet.deductBalance(totalCost);
            walletRepository.save(wallet);
            
            // Record transaction
            WalletTransaction transaction = new WalletTransaction();
            transaction.setWallet(wallet);
            transaction.setAmount(totalCost.negate()); // Negative for debit
            transaction.setTransactionType("DEBIT");
            transaction.setStatus("SUCCESS");
            transaction.setPaymentGatewayReference(
                String.format("%s_SESSION_%d_MIN", activeSession.getSessionType(), totalMinutes)
            );
            walletTransactionRepository.save(transaction);
            
            // Update video session (skip if not created)
            // VideoSession session = videoSessionRepository.findByRoomId(request.getSessionId())
            //         .orElse(null);
            // if (session != null) {
            //     session.setEndedAt(endTime);
            //     session.setStatus(VideoSessionStatus.ENDED);
            //     session.setMeta(String.format("Duration: %d minutes, Cost: ₹%.2f", totalMinutes, totalCost));
            //     videoSessionRepository.save(session);
            // }
            
            log.info("Session ended successfully: {} - Duration: {} min, Cost: ₹{}", 
                    request.getSessionId(), totalMinutes, totalCost);
            
            return SessionStatusResponse.builder()
                    .sessionId(request.getSessionId())
                    .status("ENDED")
                    .currentBalance(wallet.getBalance())
                    .totalCost(totalCost)
                    .durationMinutes((int) totalMinutes)
                    .message(String.format("Session ended. Duration: %d minutes, Cost: ₹%.2f", totalMinutes, totalCost))
                    .build();
                    
        } catch (InsufficientBalanceException e) {
            log.error("Insufficient balance during session end: {}", request.getSessionId());
            return SessionStatusResponse.builder()
                    .sessionId(request.getSessionId())
                    .status("INSUFFICIENT_BALANCE")
                    .currentBalance(wallet.getBalance())
                    .message("Session ended due to insufficient balance")
                    .build();
        }
    }
    
    @Transactional
    public SessionStatusResponse checkSessionBalance(String sessionId) {
        ActiveSession activeSession = activeSessions.get(sessionId);
        if (activeSession == null) {
            return SessionStatusResponse.builder()
                    .sessionId(sessionId)
                    .status("NOT_FOUND")
                    .message("Session not found")
                    .build();
        }
        
        // Get current wallet balance
        User user = userRepository.findById(activeSession.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        
        // Calculate estimated remaining time
        int estimatedMinutes = wallet.getBalance()
                .divide(activeSession.getRatePerMinute(), 0, BigDecimal.ROUND_DOWN)
                .intValue();
        
        // Check if balance is low (less than 2 minutes)
        boolean isLowBalance = estimatedMinutes < 2;
        
        return SessionStatusResponse.builder()
                .sessionId(sessionId)
                .status(isLowBalance ? "LOW_BALANCE" : "ACTIVE")
                .currentBalance(wallet.getBalance())
                .ratePerMinute(activeSession.getRatePerMinute())
                .estimatedMinutes(estimatedMinutes)
                .message(isLowBalance ? 
                    "Low balance! Please add money to continue." : 
                    "Session active")
                .build();
    }
    
    // Scheduled task to check and auto-end sessions with insufficient balance
    @Scheduled(fixedRate = 30000) // Check every 30 seconds
    @Async
    public void checkActiveSessions() {
        for (ActiveSession session : activeSessions.values()) {
            try {
                SessionStatusResponse status = checkSessionBalance(session.getSessionId());
                
                // Auto-end session if balance is insufficient
                if ("LOW_BALANCE".equals(status.getStatus()) && status.getEstimatedMinutes() < 1) {
                    log.warn("Auto-ending session due to insufficient balance: {}", session.getSessionId());
                    
                    EndSessionRequest endRequest = new EndSessionRequest();
                    endRequest.setSessionId(session.getSessionId());
                    endRequest.setReason("INSUFFICIENT_BALANCE");
                    
                    endSession(endRequest);
                }
            } catch (Exception e) {
                log.error("Error checking session balance: {}", session.getSessionId(), e);
            }
        }
    }
    
    private BigDecimal getRateForSessionType(String sessionType) {
        return switch (sessionType.toUpperCase()) {
            case "AUDIO_CALL", "CALL" -> CALL_RATE_PER_MINUTE;
            case "VIDEO_CALL", "VIDEO" -> VIDEO_CALL_RATE_PER_MINUTE;
            case "CHAT" -> CHAT_RATE_PER_MINUTE;
            default -> CALL_RATE_PER_MINUTE;
        };
    }
    
    // Inner class for tracking active sessions
    private static class ActiveSession {
        private final String sessionId;
        private final UUID userId;
        private final UUID mentorId;
        private final String sessionType;
        private final BigDecimal ratePerMinute;
        private final LocalDateTime startTime;
        private final BigDecimal initialBalance;
        
        public ActiveSession(String sessionId, UUID userId, UUID mentorId, String sessionType, 
                           BigDecimal ratePerMinute, LocalDateTime startTime, BigDecimal initialBalance) {
            this.sessionId = sessionId;
            this.userId = userId;
            this.mentorId = mentorId;
            this.sessionType = sessionType;
            this.ratePerMinute = ratePerMinute;
            this.startTime = startTime;
            this.initialBalance = initialBalance;
        }
        
        // Getters
        public String getSessionId() { return sessionId; }
        public UUID getUserId() { return userId; }
        public UUID getMentorId() { return mentorId; }
        public String getSessionType() { return sessionType; }
        public BigDecimal getRatePerMinute() { return ratePerMinute; }
        public LocalDateTime getStartTime() { return startTime; }
        public BigDecimal getInitialBalance() { return initialBalance; }
    }
}