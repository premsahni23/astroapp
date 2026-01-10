package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RatingRepository extends BaseRepository<Rating> {

    // Find all ratings given by a specific user
    List<Rating> findByUserId(UUID userId);

    // Find all ratings received by a specific mentor
    List<Rating> findByMentorId(UUID mentorId);

    // Find all ratings with a specific rating value
    List<Rating> findByRating(Integer rating);

    // Find all reviews containing a specific keyword (if needed)
    List<Rating> findByReviewContaining(String keyword);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.mentor.id = :mentorId")
    Double getAverageRatingByMentor(@Param("mentorId") UUID mentorId);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.mentor.id = :mentorId")
    Integer countRatingsByMentor(@Param("mentorId") UUID mentorId);
}
