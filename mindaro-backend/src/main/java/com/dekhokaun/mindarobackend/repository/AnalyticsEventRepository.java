package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.AnalyticsEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AnalyticsEventRepository extends BaseRepository<AnalyticsEvent> {
    @Query("select count(e) from AnalyticsEvent e")
    long countAll();

    @Query("select e.eventName, count(e) from AnalyticsEvent e group by e.eventName")
    List<Object[]> countByEventName();

    List<AnalyticsEvent> findByUserIdOrderByCreatedAtDesc(UUID userId);
    long countByCreatedAtAfter(LocalDateTime since);
}
