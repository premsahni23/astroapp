package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.ResourceNotFoundException;
import com.dekhokaun.mindarobackend.model.*;
import com.dekhokaun.mindarobackend.payload.request.FeedbackRequest;
import com.dekhokaun.mindarobackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final BannerRepository bannerRepository;
    private final TestimonialRepository testimonialRepository;
    private final FaqRepository faqRepository;
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    public List<Banner> banners() {
        return bannerRepository.findByIsActiveTrueOrderByCreatedAtDesc();
    }

    public List<Testimonial> testimonials() {
        return testimonialRepository.findByIsActiveTrueOrderByCreatedAtDesc();
    }

    public List<Faq> faqs() {
        return faqRepository.findByIsActiveTrueOrderByCreatedAtDesc();
    }

    public Feedback submitFeedback(FeedbackRequest request) {
        Feedback f = new Feedback();
        if (request.getUserId() != null) {
            User u = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            f.setUser(u);
        }
        f.setSubject(request.getSubject());
        f.setMessage(request.getMessage());
        return feedbackRepository.save(f);
    }
}
