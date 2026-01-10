package com.dekhokaun.mindarobackend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class WalletTransactionsResponse {
    private UUID userId;
    private List<WalletTransactionResponse> transactions;
}
