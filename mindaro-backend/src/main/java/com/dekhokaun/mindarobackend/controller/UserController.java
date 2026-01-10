package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.request.UpdateUserRequest;
import com.dekhokaun.mindarobackend.payload.request.UserRequest;
import com.dekhokaun.mindarobackend.payload.response.UserResponse;
import com.dekhokaun.mindarobackend.service.UserService;
import com.dekhokaun.mindarobackend.utils.ObjectMapperUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Controller", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new user", description = "Registers a new user in the system")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.authenticateOrRegister(request));
    }

    @Operation(summary = "User login", description = "Authenticates an existing user")
    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.loginUser(request));
    }

    @Operation(summary = "Update user profile", description = "Updates the existing user profile")
    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUserProfile(@RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUserProfile(request));
    }

    @Operation(summary = "Get user by email", description = "Retrieves a user profile by their email address")
    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(
            @Parameter(description = "Email address of the user", required = true) @PathVariable String email) {
        UserResponse response = ObjectMapperUtils.map(userService.getUserByEmail(email), UserResponse.class);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a user", description = "Deletes a user by id (Admin only)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
