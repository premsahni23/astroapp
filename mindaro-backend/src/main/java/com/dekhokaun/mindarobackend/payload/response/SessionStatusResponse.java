package com.dekhokaun.mindarobackend.payload.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SessionStatusResponse {
    
    private String sessionId;
    private String status; // STARTED, ACTIVE, LOW_BALANCE, ENDED, INSUFFICIENT_BALANCE, NOT_FOUND
    private BigDecimal currentBalance;
    private BigDecimal ratePerMinute;
    private Integer estimatedMinutes;
    private BigDecimal totalCost;
    private Integer durationMinutes;
    private String message;
    
    // Helper method to check if session should show low balance warning
    public boolean isLowBalance() {
        return "LOW_BALANCE".equals(status) || 
               (estimatedMinutes != null && estimatedMinutes < 2);
    }
    
    // Helper method to check if session should be terminated
    public boolean shouldTerminate() {
        return "INSUFFICIENT_BALANCE".equals(status) || 
               (estimatedMinutes != null && estimatedMinutes < 1);
    }
}