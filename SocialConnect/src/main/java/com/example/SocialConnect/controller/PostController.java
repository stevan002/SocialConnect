package com.example.SocialConnect.controller;

import com.example.SocialConnect.dto.http.ApiResponse;
import com.example.SocialConnect.dto.post.CreatePostRequest;
import com.example.SocialConnect.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping("/create-post")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPost(@RequestBody @Valid CreatePostRequest post, Principal principal) {
        String username = principal.getName();
        postService.createPost(post, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Successfully created post"));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getPosts() {
        return ResponseEntity.ok(postService.getPosts());
    }

    @GetMapping("/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getPosts(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @DeleteMapping("/delete-post/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deletePost(@PathVariable Long id, Principal principal) {
        String username = principal.getName();
        postService.deletePost(id, username);
        return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted post"));
    }
}
