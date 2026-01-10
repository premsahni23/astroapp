package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.ResourceNotFoundException;
import com.dekhokaun.mindarobackend.model.Mentor;
import com.dekhokaun.mindarobackend.model.Rating;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.payload.request.ReviewRequest;
import com.dekhokaun.mindarobackend.payload.request.ReviewUpdateRequest;
import com.dekhokaun.mindarobackend.repository.MentorRepository;
import com.dekhokaun.mindarobackend.repository.RatingRepository;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;

    public Rating create(ReviewRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Mentor mentor = mentorRepository.findById(request.getMentorId())
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found"));

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setMentor(mentor);
        rating.setRating(request.getRating());
        rating.setReview(request.getReview());
        rating.setIsApproved(true);

        Rating saved = ratingRepository.save(rating);
        refreshMentorRating(mentor.getId());
        return saved;
    }

    public List<Rating> byMentor(UUID mentorId) {
        return ratingRepository.findByMentorId(mentorId);
    }

    public Rating update(UUID ratingId, ReviewUpdateRequest request) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        if (request.getRating() != null) rating.setRating(request.getRating());
        if (request.getReview() != null) rating.setReview(request.getReview());

        Rating saved = ratingRepository.save(rating);
        refreshMentorRating(rating.getMentor().getId());
        return saved;
    }

    public void delete(UUID ratingId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        UUID mentorId = rating.getMentor().getId();
        ratingRepository.delete(rating);
        refreshMentorRating(mentorId);
    }

    private void refreshMentorRating(UUID mentorId) {
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found"));
        Double avg = ratingRepository.getAverageRatingByMentor(mentorId);
        Integer count = ratingRepository.countRatingsByMentor(mentorId);
        mentor.setRating(avg == null ? 0.0 : avg);
        mentor.setRatingCount(count == null ? 0 : count);
        mentorRepository.save(mentor);
    }
}
