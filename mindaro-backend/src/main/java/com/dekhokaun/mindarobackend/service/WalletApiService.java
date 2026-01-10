package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.ResourceNotFoundException;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.model.Wallet;
import com.dekhokaun.mindarobackend.model.WalletTransaction;
import com.dekhokaun.mindarobackend.payload.request.WalletAddMoneyRequest;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import com.dekhokaun.mindarobackend.repository.WalletRepository;
import com.dekhokaun.mindarobackend.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletApiService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final UserRepository userRepository;

    public Wallet getOrCreate(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return walletRepository.findByUser(user)
                .orElseGet(() -> {
                    Wallet w = new Wallet();
                    w.setUser(user);
                    w.setBalance(BigDecimal.ZERO);
                    return walletRepository.save(w);
                });
    }

    public Wallet addMoney(WalletAddMoneyRequest request) {
        Wallet wallet = getOrCreate(request.getUserId());
        wallet.addBalance(request.getAmount());
        Wallet saved = walletRepository.save(wallet);

        WalletTransaction tx = new WalletTransaction();
        tx.setWallet(saved);
        tx.setTransactionType("CREDIT");
        tx.setAmount(request.getAmount());
        tx.setStatus("SUCCESS");
        tx.setPaymentGatewayReference(request.getMethod());
        walletTransactionRepository.save(tx);
        return saved;
    }

    public List<WalletTransaction> transactions(UUID userId) {
        Wallet wallet = getOrCreate(userId);
        return walletTransactionRepository.findByWallet(wallet);
    }
}
