package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.CreditBalance;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CreditBalanceRepository extends BaseRepository<CreditBalance> {
    Optional<CreditBalance> findByUserId(UUID userId);
}
