package com.example.SocialConnect.controller;

import com.example.SocialConnect.dto.http.ApiResponse;
import com.example.SocialConnect.dto.user.UserLoginRequest;
import com.example.SocialConnect.dto.user.UserRegisterRequest;
import com.example.SocialConnect.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final AuthService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody @Valid UserRegisterRequest request
    ){
        authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Korisnik je registrovan"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody UserLoginRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
