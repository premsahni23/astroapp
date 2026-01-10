package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.InvalidAuthException;
import com.dekhokaun.mindarobackend.exception.InvalidRequestException;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.model.UserType;
import com.dekhokaun.mindarobackend.model.Wallet;
import com.dekhokaun.mindarobackend.payload.request.UpdateUserRequest;
import com.dekhokaun.mindarobackend.payload.request.UserRequest;
import com.dekhokaun.mindarobackend.payload.response.UserResponse;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import com.dekhokaun.mindarobackend.repository.WalletRepository;
import com.dekhokaun.mindarobackend.utils.RegexUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public UserResponse authenticateOrRegister(UserRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            // User Exists: Verify via Google or Password
            User user = existingUser.get();

            if ("google".equalsIgnoreCase(request.getMethod())) {
                if (!request.getToken().equals(user.getToken())) {
                    throw new InvalidAuthException("Invalid Google authentication token");
                }
            } else {
                if (!passwordEncoder.matches(request.getPassword(), user.getPwd())) {
                    throw new InvalidRequestException("Invalid password");
                }
            }
            return mapToUserResponse(user);
        }

        // User Doesn't Exist: Create New User (only for registration)
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setName(request.getName());
        newUser.setPwd(passwordEncoder.encode(request.getPassword()));
        newUser.setCountry(request.getCountry());

        String mobile = request.getMobile();
        if (mobile != null && !mobile.isEmpty() && RegexUtils.isValidPhoneNumber(request.getMobile(), "IN")) {
            newUser.setMobile(Long.valueOf(request.getMobile()));
        }
        
        User savedUser = userRepository.save(newUser);
        
        // Create wallet for new user with initial balance
        createWalletForUser(savedUser);

        return mapToUserResponse(savedUser);
    }
    
    private void createWalletForUser(User user) {
        try {
            // Check if wallet already exists
            if (walletRepository.findByUser(user).isEmpty()) {
                Wallet wallet = new Wallet();
                wallet.setUser(user);
                wallet.setBalance(new BigDecimal("50.00")); // Initial balance of ₹50
                walletRepository.save(wallet);
            }
        } catch (Exception e) {
            // Log error but don't fail user creation
            System.err.println("Failed to create wallet for user: " + user.getEmail() + " - " + e.getMessage());
        }
    }

    @Transactional
    public UserResponse loginUser(UserRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isEmpty()) {
            throw new InvalidRequestException("User not found with email: " + request.getEmail());
        }

        User user = existingUser.get();

        if ("google".equalsIgnoreCase(request.getMethod())) {
            if (!request.getToken().equals(user.getToken())) {
                throw new InvalidAuthException("Invalid Google authentication token");
            }
        } else {
            if (!passwordEncoder.matches(request.getPassword(), user.getPwd())) {
                throw new InvalidRequestException("Invalid password");
            }
        }
        
        return mapToUserResponse(user);
    }

    public UserResponse getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        return mapToUserResponse(user);
    }

    @Transactional
    public UserResponse updateUserProfile(UpdateUserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        boolean hasChanges = false;

        if (request.getName() != null && RegexUtils.isValidUsername(request.getName())) {
            user.setName(request.getName());
            hasChanges = true;
        }

        if (request.getMobile() != null && RegexUtils.isValidPhoneNumber(request.getMobile(), "IN")) {
            user.setMobile(Long.valueOf(request.getMobile()));
            hasChanges = true;
        }

        if (request.getCountry() != null) {
            user.setCountry(request.getCountry());
            hasChanges = true;
        }

        user.setIsProfileCompleted(true);
        hasChanges = true;

        if (hasChanges) {
            user = userRepository.save(user);
        }
        
        return mapToUserResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidRequestException("User not found with email: " + email));

        return mapToUserResponse(user);
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(UUID.fromString(id))) {
            throw new InvalidRequestException("User not found with id: " + id);
        }
        userRepository.deleteById(UUID.fromString(id));
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), String.valueOf(user.getMobile()), user.getCountry(), user.getUtype().toString(), user.getIsProfileCompleted());
    }

}
