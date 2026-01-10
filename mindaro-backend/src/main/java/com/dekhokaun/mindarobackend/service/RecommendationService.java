package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.model.Mentor;
import com.dekhokaun.mindarobackend.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final MentorRepository mentorRepository;

    public List<Mentor> recommendMentors(int limit) {
        int size = Math.max(1, Math.min(limit, 50));
        return mentorRepository.findAll(PageRequest.of(0, size, Sort.by("rating").descending()))
                .getContent();
    }
}
