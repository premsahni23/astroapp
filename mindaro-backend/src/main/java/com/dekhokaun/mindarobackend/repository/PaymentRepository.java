package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends BaseRepository<Payment> {
    List<Payment> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
