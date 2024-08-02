package com.example.SocialConnect.service;

import com.example.SocialConnect.dto.http.ApiResponse;
import com.example.SocialConnect.dto.reaction.CreateReactionRequest;
import com.example.SocialConnect.exception.BadRequestException;
import com.example.SocialConnect.model.*;
import com.example.SocialConnect.repository.CommentRepository;
import com.example.SocialConnect.repository.PostRepository;
import com.example.SocialConnect.repository.ReactionRepository;
import com.example.SocialConnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public ApiResponse createReaction(CreateReactionRequest reactionRequest, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("user", "Not found user with given id"));

        ReactionType reactionType;
        try {
            reactionType = ReactionType.valueOf(reactionRequest.getReactionType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("reaction", "Invalid reaction type");
        }

        Reaction existingReaction = null;
        if (reactionRequest.getPostId() != null && reactionRequest.getCommentId() == null) {
            Post post = postRepository.findById(reactionRequest.getPostId())
                    .orElseThrow(() -> new BadRequestException("post", "Not found post with given id"));
            existingReaction = reactionRepository.findByPostAndCreatedBy(post, user);
        } else if (reactionRequest.getCommentId() != null && reactionRequest.getPostId() == null) {
            Comment comment = commentRepository.findById(reactionRequest.getCommentId())
                    .orElseThrow(() -> new BadRequestException("comment", "Not found comment with given id"));
            existingReaction = reactionRepository.findByCommentAndCreatedBy(comment, user);
        } else {
            throw new BadRequestException("reaction", "Either postId or commentId must be provided, but not both");
        }

        String message;
        if (existingReaction != null) {
            if (existingReaction.getType() == reactionType) {
                reactionRepository.delete(existingReaction);
                message = "Successfully deleted reaction";
            } else {
                existingReaction.setType(reactionType);
                reactionRepository.save(existingReaction);
                message = "Successfully updated reaction";
            }
        } else {
            Reaction.ReactionBuilder reactionBuilder = Reaction.builder()
                    .type(reactionType)
                    .createdBy(user)
                    .timestamp(LocalDateTime.now());

            if (reactionRequest.getPostId() != null) {
                Post post = postRepository.findById(reactionRequest.getPostId())
                        .orElseThrow(() -> new BadRequestException("post", "Not found post with given id"));
                reactionBuilder.post(post);
            } else {
                Comment comment = commentRepository.findById(reactionRequest.getCommentId())
                        .orElseThrow(() -> new BadRequestException("comment", "Not found comment with given id"));
                reactionBuilder.comment(comment);
            }

            Reaction reaction = reactionBuilder.build();
            reactionRepository.save(reaction);
            message = "Successfully created reaction";
        }

        return new ApiResponse(true, message);
    }
}
