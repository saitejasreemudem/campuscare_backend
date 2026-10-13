
package com.campuscare.campuscare.controller;

import com.campuscare.campuscare.entity.User;
import com.campuscare.campuscare.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (user.getEmail() == null || user.getEmail().isBlank()
                || user.getPassword() == null
                || user.getPassword().length() < 8) {
            return ResponseEntity.badRequest().body(
                    Map.of("message",
                    "Enter a valid email and a password of at least 8 characters")
            );
        }

        String email = user.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("message", "Email is already registered")
            );
        }

        user.setId(null);
        user.setEmail(email);

        // Public registration always creates a student account.
        user.setRole("STUDENT");

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("message", "Registration successful")
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        if (user.getEmail() == null || user.getEmail().isBlank()
                || user.getPassword() == null
                || user.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Email and password are required")
            );
        }

        String email = user.getEmail().trim().toLowerCase();

        return userRepository.findByEmail(email)
                .filter(savedUser ->
                        savedUser.getPassword().equals(user.getPassword()))
                .<ResponseEntity<?>>map(savedUser ->
                        ResponseEntity.ok(Map.of(
                                "message", "Login successful",
                                "email", savedUser.getEmail(),
                                "role", savedUser.getRole()
                        )))
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                                Map.of("message", "Invalid email or password")
                        ));
    }
}