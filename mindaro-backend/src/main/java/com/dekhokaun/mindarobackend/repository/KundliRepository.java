package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Kundli;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KundliRepository extends BaseRepository<Kundli> {
    List<Kundli> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
