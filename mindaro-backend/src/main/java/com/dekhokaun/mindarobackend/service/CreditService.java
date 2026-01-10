package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.ResourceNotFoundException;
import com.dekhokaun.mindarobackend.model.CreditBalance;
import com.dekhokaun.mindarobackend.model.CreditTransaction;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.model.Wallet;
import com.dekhokaun.mindarobackend.model.WalletTransaction;
import com.dekhokaun.mindarobackend.payload.request.CreditsPurchaseRequest;
import com.dekhokaun.mindarobackend.repository.CreditBalanceRepository;
import com.dekhokaun.mindarobackend.repository.CreditTransactionRepository;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import com.dekhokaun.mindarobackend.repository.WalletRepository;
import com.dekhokaun.mindarobackend.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditBalanceRepository creditBalanceRepository;
    private final CreditTransactionRepository creditTransactionRepository;
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final UserRepository userRepository;

    public CreditBalance getOrCreate(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return creditBalanceRepository.findByUserId(userId)
                .orElseGet(() -> {
                    CreditBalance cb = new CreditBalance();
                    cb.setUser(user);
                    cb.setCredits(0);
                    return creditBalanceRepository.save(cb);
                });
    }

    public CreditBalance purchase(CreditsPurchaseRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found. Create wallet or add money first."));

        // Deduct money from wallet
        wallet.deductBalance(request.getAmount());
        Wallet savedWallet = walletRepository.save(wallet);

        WalletTransaction wtx = new WalletTransaction();
        wtx.setWallet(savedWallet);
        wtx.setTransactionType("DEBIT");
        wtx.setAmount(request.getAmount());
        wtx.setStatus("SUCCESS");
        wtx.setPaymentGatewayReference(request.getRef());
        walletTransactionRepository.save(wtx);

        CreditBalance cb = getOrCreate(user.getId());
        cb.setCredits(cb.getCredits() + request.getCredits());
        CreditBalance savedCb = creditBalanceRepository.save(cb);

        CreditTransaction ctx = new CreditTransaction();
        ctx.setUser(user);
        ctx.setCredits(request.getCredits());
        ctx.setType("purchase");
        ctx.setRef(request.getRef());
        creditTransactionRepository.save(ctx);

        return savedCb;
    }

    public List<CreditTransaction> transactions(UUID userId) {
        return creditTransactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
