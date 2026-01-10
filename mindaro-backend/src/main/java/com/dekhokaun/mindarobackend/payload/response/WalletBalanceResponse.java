package com.dekhokaun.mindarobackend.payload.response;

import com.dekhokaun.mindarobackend.model.Wallet;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class WalletBalanceResponse {

    private UUID walletId;
    private UUID userId;
    private BigDecimal balance;

    public static WalletBalanceResponse from(Wallet wallet) {
        return new WalletBalanceResponse(
                wallet.getId(),
                wallet.getUser().getId(),
                wallet.getBalance()
        );
    }
}
