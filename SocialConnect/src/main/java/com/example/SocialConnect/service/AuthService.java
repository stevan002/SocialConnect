package com.example.SocialConnect.service;

import com.example.SocialConnect.dto.user.AuthenticationResponse;
import com.example.SocialConnect.model.Role;
import com.example.SocialConnect.model.User;
import com.example.SocialConnect.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(User request){

        // Validate input
        validateUserRequest(request);

        // Check if username is already taken
        if (repository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username must be unique.");
        }

        // Check if displayName is already taken
        if (repository.existsByDisplayName(request.getDisplayName())) {
            throw new IllegalArgumentException("Display name must be unique.");
        }

        // Create a new User object
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .displayName(request.getDisplayName())
                .role(Role.USER)
                .description(request.getDescription())
                .build();

        // Save the user in the database
        user = repository.save(user);

        // Generate JWT token
        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }

    @Transactional
    public AuthenticationResponse authenticate(User request){

        // Find user by username
        User user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Wrong username or password."));

        // Check if passwords match
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Wrong username or password.");
        }

        // Perform authentication
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Generate JWT token
        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new IllegalArgumentException("Not found JWT token in header 'Authorization'.");
    }

    private void validateUserRequest(User request) {
        if (request == null ||
                request.getFirstName() == null ||
                request.getLastName() == null ||
                request.getUsername() == null ||
                request.getPassword() == null ||
                request.getEmail() == null ||
                request.getDisplayName() == null) {
            throw new IllegalArgumentException("Must input all data for user.");
        }
    }
}
