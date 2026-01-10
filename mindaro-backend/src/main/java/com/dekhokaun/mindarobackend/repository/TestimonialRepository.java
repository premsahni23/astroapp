package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Testimonial;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestimonialRepository extends BaseRepository<Testimonial> {
    List<Testimonial> findByIsActiveTrueOrderByCreatedAtDesc();
}
