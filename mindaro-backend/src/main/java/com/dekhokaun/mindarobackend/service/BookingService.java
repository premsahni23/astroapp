package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.ResourceNotFoundException;
import com.dekhokaun.mindarobackend.model.Booking;
import com.dekhokaun.mindarobackend.model.BookingStatus;
import com.dekhokaun.mindarobackend.model.Mentor;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.payload.request.BookingRequest;
import com.dekhokaun.mindarobackend.repository.BookingRepository;
import com.dekhokaun.mindarobackend.repository.MentorRepository;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;

    public Booking create(BookingRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Mentor mentor = mentorRepository.findById(request.getMentorId())
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setMentor(mentor);
        booking.setScheduledAt(request.getScheduledAt());
        booking.setSessionType(request.getSessionType());
        booking.setAmount(request.getAmount());
        booking.setNotes(request.getNotes());
        booking.setStatus(BookingStatus.PENDING);
        return bookingRepository.save(booking);
    }

    public List<Booking> byUser(UUID userId) {
        return bookingRepository.findByUserIdOrderByScheduledAtDesc(userId);
    }

    public List<Booking> byMentor(UUID mentorId) {
        return bookingRepository.findByMentorIdOrderByScheduledAtDesc(mentorId);
    }

    public Booking updateStatus(UUID id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public void cancel(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }
}
