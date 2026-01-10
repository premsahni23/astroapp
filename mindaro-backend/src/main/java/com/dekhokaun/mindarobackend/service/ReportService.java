package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final VideoSessionRepository videoSessionRepository;
    private final FeedbackRepository feedbackRepository;

    public Map<String, Object> usage() {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("users", userRepository.count());
        res.put("mentors", mentorRepository.count());
        res.put("bookings", bookingRepository.count());
        res.put("payments", paymentRepository.count());
        res.put("video_sessions", videoSessionRepository.count());
        res.put("feedback", feedbackRepository.count());
        return res;
    }
}
