package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Booking;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends BaseRepository<Booking> {
    List<Booking> findByUserIdOrderByScheduledAtDesc(UUID userId);
    List<Booking> findByMentorIdOrderByScheduledAtDesc(UUID mentorId);
}
