package com.dekhokaun.mindarobackend.payload.response;

import com.dekhokaun.mindarobackend.model.CreditBalance;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CreditsPurchaseResponse {
    private UUID userId;
    private UUID creditBalanceId;
    private Integer credits;

    public static CreditsPurchaseResponse from(CreditBalance balance) {
        return new CreditsPurchaseResponse(
                balance.getUser().getId(),
                balance.getId(),
                balance.getCredits()
        );
    }
}
