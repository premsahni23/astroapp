package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends BaseRepository<Order> {
    List<Order> findByUserId(UUID userId);

    List<Order> findByTransactionId(UUID transactionId);
}
