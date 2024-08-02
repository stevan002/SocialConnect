package com.example.SocialConnect.controller;

import com.example.SocialConnect.dto.http.ApiResponse;
import com.example.SocialConnect.dto.reaction.CreateReactionRequest;
import com.example.SocialConnect.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reactions")
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping("/create-reaction")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createReaction(@RequestBody CreateReactionRequest reactionRequest, Principal principal){
        String username = principal.getName();
         ApiResponse response =  reactionService.createReaction(reactionRequest, username);
         return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
