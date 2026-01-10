package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.InvalidRequestException;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.model.UserType;
import com.dekhokaun.mindarobackend.payload.request.CreateAdminRequest;
import com.dekhokaun.mindarobackend.payload.request.CreateUserRequest;
import com.dekhokaun.mindarobackend.payload.request.AdminUpdateUserRequest;
import com.dekhokaun.mindarobackend.payload.response.AdminUserResponse;
import com.dekhokaun.mindarobackend.payload.response.PaginatedResponse;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import com.dekhokaun.mindarobackend.utils.RegexUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public AdminUserResponse createAdmin(CreateAdminRequest request, String creatorEmail) {
        // Verify that creator is an admin
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new InvalidRequestException("Creator not found"));

        if (creator.getUtype() != UserType.ADMIN) {
            throw new InvalidRequestException("Only admins can create other admins");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidRequestException("Email already exists");
        }

        // Create new admin user
        User newAdmin = new User();
        newAdmin.setEmail(request.getEmail());
        newAdmin.setName(request.getName());
        newAdmin.setPwd(passwordEncoder.encode(request.getPassword()));
        newAdmin.setCountry(request.getCountry());
        newAdmin.setUtype(UserType.ADMIN);
        newAdmin.setIsProfileCompleted(true);

        String mobile = request.getMobile();
        if (mobile != null && !mobile.isEmpty() && RegexUtils.isValidPhoneNumber(mobile, "IN")) {
            newAdmin.setMobile(Long.valueOf(mobile));
        }

        userRepository.save(newAdmin);
        return mapToAdminUserResponse(newAdmin);
    }

    public PaginatedResponse<AdminUserResponse> getAllUsers(String adminEmail, String q, String role, String status, Integer page, Integer size) {
        // Verify that requester is an admin
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new InvalidRequestException("Admin not found"));

        if (admin.getUtype() != UserType.ADMIN) {
            throw new InvalidRequestException("Only admins can view all users");
        }

        int safeSize = (size == null || size < 1) ? 10 : Math.min(size, 200);
        int safePage = (page == null || page < 1) ? 1 : page;
        int pageIndex = safePage - 1; // incoming is 1-based

        UserType utype = parseUserType(role);
        String statusUpper = (status == null || status.isBlank()) ? null : status.trim().toUpperCase();

        Pageable pageable = PageRequest.of(pageIndex, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> result = userRepository.adminSearchUsers(q, utype, statusUpper, pageable);

        List<AdminUserResponse> items = result.getContent().stream()
                .map(this::mapToAdminUserResponse)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(items, safePage, safeSize, result.getTotalElements());
    }

    @Transactional
    public void deleteUser(String userEmail, String adminEmail) {
        // Verify that requester is an admin
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new InvalidRequestException("Admin not found"));

        if (admin.getUtype() != UserType.ADMIN) {
            throw new InvalidRequestException("Only admins can delete users");
        }

        User userToDelete = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        // Prevent deleting another admin
        if (userToDelete.getUtype() == UserType.ADMIN && !adminEmail.equals(userEmail)) {
            throw new InvalidRequestException("Admins cannot delete other admins");
        }

        userRepository.delete(userToDelete);
    }

    @Transactional
    public AdminUserResponse createUser(CreateUserRequest request, String adminEmail) {
        // Verify that creator is an admin
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new InvalidRequestException("Admin not found"));

        if (admin.getUtype() != UserType.ADMIN) {
            throw new InvalidRequestException("Only admins can create users");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidRequestException("Email already exists");
        }

        // Create new user
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setName(request.getName());
        newUser.setPwd(passwordEncoder.encode(request.getPassword()));
        newUser.setCountry(request.getCountry());
        newUser.setUtype(parseUserTypeOrDefault(request.getUtype(), UserType.CUSTOMER));
        newUser.setIsProfileCompleted(request.getIs_profile_completed() != null ? request.getIs_profile_completed() : false);

        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            newUser.setStatus(request.getStatus().trim().toLowerCase());
        }

        String mobile = request.getMobile();
        if (mobile != null && !mobile.isEmpty() && RegexUtils.isValidPhoneNumber(mobile, "IN")) {
            newUser.setMobile(Long.valueOf(mobile));
        }

        userRepository.save(newUser);
        return mapToAdminUserResponse(newUser);
    }

    public AdminUserResponse getUserByEmail(String userEmail, String adminEmail) {
        // Verify that requester is an admin
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new InvalidRequestException("Admin not found"));

        if (admin.getUtype() != UserType.ADMIN) {
            throw new InvalidRequestException("Only admins can view user details");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        return mapToAdminUserResponse(user);
    }

    @Transactional
    public AdminUserResponse updateUser(String userEmail, AdminUpdateUserRequest request, String adminEmail) {
        // Verify that updater is an admin
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new InvalidRequestException("Admin not found"));

        if (admin.getUtype() != UserType.ADMIN) {
            throw new InvalidRequestException("Only admins can update users");
        }

        User userToUpdate = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        // Check if all fields are empty/null - if so, return user as is
        boolean allFieldsEmpty = (request.getName() == null || request.getName().trim().isEmpty()) &&
                (request.getMobile() == null || request.getMobile().trim().isEmpty()) &&
                (request.getPassword() == null || request.getPassword().trim().isEmpty()) &&
                (request.getCountry() == null || request.getCountry().trim().isEmpty()) &&
                (request.getUtype() == null || request.getUtype().trim().isEmpty()) &&
                (request.getStatus() == null || request.getStatus().trim().isEmpty()) &&
                (request.getIs_profile_completed() == null);

        if (allFieldsEmpty) {
            return mapToAdminUserResponse(userToUpdate);
        }

        // Update user fields only if they are provided and not empty
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            userToUpdate.setName(request.getName());
        }

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            userToUpdate.setPwd(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getCountry() != null && !request.getCountry().trim().isEmpty()) {
            userToUpdate.setCountry(request.getCountry());
        }

        if (request.getUtype() != null && !request.getUtype().trim().isEmpty()) {
            userToUpdate.setUtype(parseUserTypeOrDefault(request.getUtype(), userToUpdate.getUtype()));
        }

        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            userToUpdate.setStatus(request.getStatus().trim().toLowerCase());
        }

        if (request.getIs_profile_completed() != null) {
            userToUpdate.setIsProfileCompleted(request.getIs_profile_completed());
        }

        String mobile = request.getMobile();
        if (mobile != null && !mobile.isEmpty() && RegexUtils.isValidPhoneNumber(mobile, "IN")) {
            userToUpdate.setMobile(Long.valueOf(mobile));
        }

        userRepository.save(userToUpdate);
        return mapToAdminUserResponse(userToUpdate);
    }

    private AdminUserResponse mapToAdminUserResponse(User user) {
        long numericId = user.getUmid() != null ? user.getUmid() : 0L;

        String status = normalizeStatus(user.getStatus());
        double rating = user.getRating() != null ? user.getRating().doubleValue() : 0.0;
        int ratingCount = user.getRatingcount() != null ? user.getRatingcount() : 0;
        String createdAt = user.getCreatedAt() != null
                ? user.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                : null;

        return new AdminUserResponse(
                numericId,
                user.getName(),
                user.getEmail(),
                user.getMobile() != null ? String.valueOf(user.getMobile()) : "",
                user.getCountry(),
                user.getUtype() != null ? user.getUtype().name() : "CUSTOMER",
                status,
                Boolean.TRUE.equals(user.getIsProfileCompleted()),
                rating,
                ratingCount,
                createdAt
        );
    }

    private String normalizeStatus(String raw) {
        if (raw == null) return "ACTIVE";
        String s = raw.trim();
        if (s.isEmpty()) return "ACTIVE";
        // DB uses 'active' in some places
        if (s.equalsIgnoreCase("active")) return "ACTIVE";
        if (s.equalsIgnoreCase("inactive")) return "INACTIVE";
        // fallback: best-effort
        return s.toUpperCase();
    }

    private UserType parseUserType(String role) {
        if (role == null || role.isBlank()) return null;
        return parseUserTypeOrDefault(role, null);
    }

    private UserType parseUserTypeOrDefault(String role, UserType def) {
        if (role == null || role.isBlank()) return def;
        String r = role.trim().toUpperCase();
        try {
            return UserType.valueOf(r);
        } catch (IllegalArgumentException e) {
            // allow dashboard strings only; if invalid, fallback
            return def;
        }
    }
}