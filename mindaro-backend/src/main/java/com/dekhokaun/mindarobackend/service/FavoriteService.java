package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.ResourceNotFoundException;
import com.dekhokaun.mindarobackend.model.Favorite;
import com.dekhokaun.mindarobackend.model.Mentor;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.payload.request.FavoriteRequest;
import com.dekhokaun.mindarobackend.repository.FavoriteRepository;
import com.dekhokaun.mindarobackend.repository.MentorRepository;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;

    public Favorite add(FavoriteRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Mentor mentor = mentorRepository.findById(request.getMentorId())
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found"));

        return favoriteRepository.findByUserIdAndMentorId(user.getId(), mentor.getId())
                .orElseGet(() -> {
                    Favorite f = new Favorite();
                    f.setUser(user);
                    f.setMentor(mentor);
                    return favoriteRepository.save(f);
                });
    }

    public List<Favorite> list(UUID userId) {
        return favoriteRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void delete(UUID favoriteId) {
        Favorite f = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));
        favoriteRepository.delete(f);
    }
}
