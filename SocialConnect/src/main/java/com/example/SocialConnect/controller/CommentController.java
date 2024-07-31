package com.example.SocialConnect.controller;

import com.example.SocialConnect.dto.comment.CreateCommentRequest;
import com.example.SocialConnect.dto.http.ApiResponse;
import com.example.SocialConnect.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create-comment")
    public ResponseEntity<?> createComment(@RequestBody @Valid CreateCommentRequest comment, Principal principal){
        String username = principal.getName();
        commentService.createComment(comment, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Comment created successfully"));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getCommentsForPost(@PathVariable Long postId){
        return ResponseEntity.ok(commentService.getCommentsForPost(postId));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<?> getComment(@PathVariable Long commentId){
        return ResponseEntity.ok(commentService.getComment(commentId));
    }

    @DeleteMapping("/delete-comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, Principal principal){
        String username = principal.getName();
        commentService.deleteComment(commentId, username);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Comment deleted successfully"));
    }
}
