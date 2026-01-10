package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Faq;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends BaseRepository<Faq> {
    List<Faq> findByIsActiveTrueOrderByCreatedAtDesc();
}
