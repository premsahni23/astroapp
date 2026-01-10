package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.ResourceNotFoundException;
import com.dekhokaun.mindarobackend.model.Payment;
import com.dekhokaun.mindarobackend.model.PaymentStatus;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.payload.request.PaymentCreateRequest;
import com.dekhokaun.mindarobackend.payload.request.PaymentRefundRequest;
import com.dekhokaun.mindarobackend.payload.request.PaymentVerifyRequest;
import com.dekhokaun.mindarobackend.repository.PaymentRepository;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public Payment create(PaymentCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setProvider(request.getProvider());
        payment.setStatus(PaymentStatus.CREATED);
        return paymentRepository.save(payment);
    }

    public Payment verify(PaymentVerifyRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        payment.setProviderPaymentId(request.getProviderPaymentId());
        payment.setMeta(request.getMeta());
        payment.setStatus(request.isSuccess() ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        return paymentRepository.save(payment);
    }

    public List<Payment> history(UUID userId) {
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Payment refund(PaymentRefundRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        payment.setStatus(PaymentStatus.REFUNDED);
        // reason can be stored in meta
        if (request.getReason() != null) {
            String meta = payment.getMeta() == null ? "" : payment.getMeta();
            payment.setMeta(meta + "\nrefund_reason=" + request.getReason());
        }
        return paymentRepository.save(payment);
    }
}
