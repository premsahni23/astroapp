package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.model.Wallet;
import com.dekhokaun.mindarobackend.model.WalletTransaction;
import com.dekhokaun.mindarobackend.payload.request.WalletAddMoneyRequest;
import com.dekhokaun.mindarobackend.payload.response.WalletBalanceResponse;
import com.dekhokaun.mindarobackend.payload.response.WalletTransactionResponse;
import com.dekhokaun.mindarobackend.payload.response.WalletTransactionsResponse;
import com.dekhokaun.mindarobackend.service.WalletApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
@Tag(name = "Wallet", description = "Wallet balance, add-money, and transaction history")
@RequiredArgsConstructor
public class WalletController {

    private final WalletApiService walletApiService;

    @Operation(summary = "Get wallet balance", description = "Returns current wallet balance for a user")
    @GetMapping("/balance/{userId}")
    public ResponseEntity<WalletBalanceResponse> getWalletBalance(@PathVariable UUID userId) {
        Wallet wallet = walletApiService.getOrCreate(userId);
        return ResponseEntity.ok(WalletBalanceResponse.from(wallet));
    }

    @Operation(summary = "Add money to wallet", description = "Credits wallet balance for a user and records a wallet transaction")
    @PostMapping("/add-money")
    public ResponseEntity<WalletBalanceResponse> addMoney(@Valid @RequestBody WalletAddMoneyRequest request) {
        Wallet wallet = walletApiService.addMoney(request);
        return ResponseEntity.ok(WalletBalanceResponse.from(wallet));
    }

    @Operation(summary = "Wallet transaction history", description = "Returns wallet transaction history for a user")
    @PostMapping("/transactions")
    public ResponseEntity<WalletTransactionsResponse> transactions(@Valid @RequestBody WalletAddMoneyRequest request) {
        List<WalletTransaction> txs = walletApiService.transactions(request.getUserId());
        List<WalletTransactionResponse> mapped = txs.stream().map(WalletTransactionResponse::from).toList();
        return ResponseEntity.ok(new WalletTransactionsResponse(request.getUserId(), mapped));
    }
}
