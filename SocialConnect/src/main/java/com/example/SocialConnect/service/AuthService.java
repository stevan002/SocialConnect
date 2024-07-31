package com.example.SocialConnect.service;

import com.example.SocialConnect.dto.user.AuthenticationResponse;
import com.example.SocialConnect.dto.user.UserLoginRequest;
import com.example.SocialConnect.dto.user.UserRegisterRequest;
import com.example.SocialConnect.exception.BadRequestException;
import com.example.SocialConnect.model.Role;
import com.example.SocialConnect.model.User;
import com.example.SocialConnect.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void register(UserRegisterRequest request){

        if (repository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("username", "Username must be unique.");
        }

        if (repository.existsByEmail(request.getUsername())) {
            throw new BadRequestException("username", "Username must be unique.");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.USER)
                .description(request.getDescription())
                .build();

        repository.save(user);
    }

    @Transactional
    public AuthenticationResponse authenticate(UserLoginRequest request){

        User user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("username", "Wrong username"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("password", "Wrong password.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        user.setLastLogin(LocalDateTime.now());
        repository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new BadRequestException("token", "Not found JWT token in header 'Authorization'.");
    }
}
