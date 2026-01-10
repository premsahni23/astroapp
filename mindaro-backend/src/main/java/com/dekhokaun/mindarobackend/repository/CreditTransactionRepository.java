package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.CreditTransaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CreditTransactionRepository extends BaseRepository<CreditTransaction> {
    List<CreditTransaction> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
