package com.altay.finalproject.controller;


import com.altay.finalproject.model.dto.request.AuthRequest;
import com.altay.finalproject.model.dto.response.AuthResponse;
import com.altay.finalproject.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altay.finalproject.model.dto.request.RegisterRequest;
import com.altay.finalproject.security.CustomUserDetailsService;
import com.altay.finalproject.model.entity.AppUser;
import com.altay.finalproject.repository.AppUserRepository;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final AppUserRepository appUserRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          CustomUserDetailsService userDetailsService, AppUserRepository appUserRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.appUserRepository = appUserRepository;
    }

    // Register Endpoint
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Check if user already exists
        Optional<AppUser> existingUser = appUserRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // Convert the request to AppUser entity
        AppUser newUser = new AppUser();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(new BCryptPasswordEncoder().encode(request.getPassword())); // Encoding password
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());

        // Set Role based on the request
        //Role role = Role.valueOf(request.getRole().toUpperCase()); // Convert to enum (PATRON or LIBRARIAN)
        newUser.setRole(request.getRole());

        // Save the new user
        appUserRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully");
    }

    // Login Endpoint (unchanged)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
