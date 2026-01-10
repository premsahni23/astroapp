package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.CategoryNotFoundException;
import com.dekhokaun.mindarobackend.exception.ResourceNotFoundException;
import com.dekhokaun.mindarobackend.model.Category;
import com.dekhokaun.mindarobackend.model.Mentor;
import com.dekhokaun.mindarobackend.model.Rating;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.payload.request.MentorRequest;
import com.dekhokaun.mindarobackend.payload.request.MentorUpdateRequest;
import com.dekhokaun.mindarobackend.payload.response.MentorResponse;
import com.dekhokaun.mindarobackend.repository.CategoryRepository;
import com.dekhokaun.mindarobackend.repository.MentorRepository;
import com.dekhokaun.mindarobackend.repository.RatingRepository;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;

    private final CategoryRepository  categoryRepository;

    private final UserRepository userRepository;

    private final RatingRepository ratingRepository;

    /**
     * Add a new mentor
     */
    public MentorResponse addMentor(MentorRequest request) {
        // NOTE: This endpoint isn't used by the admin-dashboard, but we keep it working.
        Mentor mentor = new Mentor();
        
        // Set required fields from request
        mentor.setName(request.getName());
        mentor.setEmail(request.getEmail());
        mentor.setMobile(request.getMobile());
        mentor.setCountry(request.getCountry());
        mentor.setPwd(request.getPassword()); // In production, this should be hashed
        mentor.setTotalexpyrs(request.getExperience());
        
        // Set optional fields from request
        mentor.setFirstname(request.getFirstName());
        mentor.setLastname(request.getLastName());
        mentor.setNationality(request.getNationality());
        mentor.setPhoto(request.getPhoto());
        mentor.setGender(request.getGender());
        mentor.setDob(request.getDob());
        mentor.setMainlanguage(request.getMainLanguage());
        mentor.setAddress(request.getAddress());
        mentor.setCity(request.getCity());
        mentor.setState(request.getState());
        mentor.setPincode(request.getPincode());
        mentor.setTimeweeklyhrs(request.getTimeWeeklyHrs());
        mentor.setPlatforminfo(request.getPlatformInfo());
        mentor.setQualification(request.getQualification());
        mentor.setMainincome(request.getMainIncome());
        mentor.setTw(request.getTwitter());
        mentor.setLinkedin(request.getLinkedin());
        mentor.setYt(request.getYoutube());
        mentor.setFb(request.getFacebook());
        mentor.setRate(request.getRate());
        
        // Set required fields with default values (these will be updated later)
        mentor.setMentorfbid(""); // Default empty string
        mentor.setRating(0.0); // Default rating, will be calculated later
        mentor.setRatingCount(0); // Default rating count, will be updated later
        
        // Generate a unique umid (numeric ID for compatibility)
        mentor.setUmid((int) (System.currentTimeMillis() % Integer.MAX_VALUE));

        // Best-effort mapping for legacy field "category" -> categories
        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            categoryRepository.findByName(request.getCategory())
                    .ifPresent(cat -> mentor.setCategories(Set.of(cat)));
        }

        Mentor saved = mentorRepository.save(mentor);
        return toContract(saved);
    }

    public List<MentorResponse> getMentors() {
        return mentorRepository.findAll().stream()
                .map(this::toContract)
                .toList();
    }

    public MentorResponse getMentorById(Integer id) {
        Mentor mentor = mentorRepository.findByUmid(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with id: " + id));
        return toContract(mentor);
    }

    public MentorResponse getMentorByUuid(UUID uuid) {
        Mentor mentor = mentorRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with UUID: " + uuid));
        return toContract(mentor);
    }

    /**
     * Get paginated list of mentors by category
     */
    public Page<MentorResponse> getMentorsByCategory(String language, UUID categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));

        Set<Category> categories = Set.of(category);

        Page<Mentor> mentorPage = mentorRepository.findByCategoriesContainingAndMainlanguage(categories, language, pageable);

        return mentorPage.map(this::toContract);
    }

    /**
     * Get mentor details by name
     */
    public MentorResponse getMentorDetails(String name) {
        Mentor mentor = mentorRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with name: " + name));
        return toContract(mentor);
    }

    public MentorResponse updateMentor(Integer id, MentorUpdateRequest request) {
        Mentor mentor = mentorRepository.findByUmid(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with id: " + id));

        if (request.getName() != null) mentor.setName(request.getName());
        if (request.getEmail() != null) mentor.setEmail(request.getEmail());
        if (request.getMobile() != null) {
            try {
                mentor.setMobile(Long.parseLong(request.getMobile().trim()));
            } catch (NumberFormatException ignored) {
                // Keep existing mobile if parsing fails
            }
        }
        if (request.getCountry() != null) mentor.setCountry(request.getCountry());
        if (request.getAbout() != null) mentor.setPlatforminfo(request.getAbout());
        if (request.getRating() != null) mentor.setRating(request.getRating());
        if (request.getRatingCount() != null) mentor.setRatingCount(request.getRatingCount());

        if (request.getStatus() != null) {
            String s = request.getStatus().trim().toUpperCase();
            if ("ACTIVE".equals(s)) mentor.setStatus(com.dekhokaun.mindarobackend.model.MentorStatus.ACTIVE);
            if ("INACTIVE".equals(s)) mentor.setStatus(com.dekhokaun.mindarobackend.model.MentorStatus.INACTIVE);
        }

        if (request.getExpertise() != null) {
            Set<Category> cats = request.getExpertise().stream()
                    .filter(n -> n != null && !n.isBlank())
                    .map(String::trim)
                    .map(categoryRepository::findByName)
                    .flatMap(java.util.Optional::stream)
                    .collect(java.util.stream.Collectors.toSet());
            mentor.setCategories(cats);
        }

        Mentor updated = mentorRepository.save(mentor);
        return toContract(updated);
    }

    private MentorResponse toContract(Mentor mentor) {
        MentorResponse res = new MentorResponse();
        res.setId(mentor.getUmid() != null ? mentor.getUmid() : Math.abs(mentor.getId().hashCode()));
        res.setName(mentor.getName());
        res.setEmail(mentor.getEmail());
        res.setMobile(mentor.getMobile() != null ? String.valueOf(mentor.getMobile()) : null);
        res.setCountry(mentor.getCountry());

        // Dashboard expects only ACTIVE/INACTIVE
        String status = "INACTIVE";
        if (mentor.getStatus() != null && mentor.getStatus() == com.dekhokaun.mindarobackend.model.MentorStatus.ACTIVE) {
            status = "ACTIVE";
        }
        res.setStatus(status);

        res.setAbout(mentor.getPlatforminfo());
        res.setExpertise(mentor.getCategories() == null ? List.of() : mentor.getCategories().stream()
                .map(Category::getName)
                .filter(n -> n != null && !n.isBlank())
                .toList());

        res.setRating(mentor.getRating());
        res.setRatingCount(mentor.getRatingCount());
        res.setCreatedAt(mentor.getCreatedAt() != null ? mentor.getCreatedAt().toString() : null);
        return res;
    }

    /**
     * Update mentor rating
     */
    @Transactional
    public void updateMentorRating(String mentorName, Integer ratingValue, UUID userId, String review) {
        Mentor mentor = mentorRepository.findByName(mentorName)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with name: " + mentorName));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Save new rating entry
        Rating rating = new Rating();
        rating.setUser(user);
        rating.setMentor(mentor);
        rating.setRating(ratingValue);
        rating.setReview(review);
        ratingRepository.save(rating);

        // Recalculate mentor's average rating
        Double avgRating = ratingRepository.getAverageRatingByMentor(mentor.getId());
        Integer totalRatings = ratingRepository.countRatingsByMentor(mentor.getId());

        mentor.setRating(avgRating);
        mentor.setRatingCount(totalRatings);

        mentorRepository.save(mentor);
    }

    /**
     * Delete a mentor by name
     */
    public void deleteMentor(String name) {
        Mentor mentor = mentorRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with name: " + name));

        mentorRepository.delete(mentor);
    }
}
