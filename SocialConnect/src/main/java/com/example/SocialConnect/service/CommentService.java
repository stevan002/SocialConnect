package com.example.SocialConnect.service;

import com.example.SocialConnect.dto.comment.CommentResponse;
import com.example.SocialConnect.dto.comment.CreateCommentRequest;
import com.example.SocialConnect.exception.BadRequestException;
import com.example.SocialConnect.indexmodel.PostIndex;
import com.example.SocialConnect.indexrepository.PostIndexRepository;
import com.example.SocialConnect.mapper.CommentMapper;
import com.example.SocialConnect.mapper.GroupMapper;
import com.example.SocialConnect.model.Comment;
import com.example.SocialConnect.model.Group;
import com.example.SocialConnect.model.Post;
import com.example.SocialConnect.model.User;
import com.example.SocialConnect.repository.CommentRepository;
import com.example.SocialConnect.repository.PostRepository;
import com.example.SocialConnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostIndexRepository postIndexRepository;

    public void createComment(CreateCommentRequest commentDto, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("username", "User not found with given username"));

        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new BadRequestException("post", "Post not found with given id"));


        Comment comment = Comment.builder()
                .text(commentDto.getText())
                .post(post)
                .createdBy(user)
                .timestamp(LocalDateTime.now())
                .build();

        commentRepository.save(comment);

        PostIndex postIndex = postIndexRepository.findByDatabaseId(post.getId())
                .orElseThrow(() -> new BadRequestException("postIndex", "Post index not found with given databaseId"));

        String commentContent;
        if(postIndex.getCommentContent() == null){
            commentContent = "";
        } else {
            commentContent = postIndex.getCommentContent();
        }

        commentContent += comment.getText() + '\n';
        postIndex.setCommentContent(commentContent);
        postIndex.setNumberOfComments(postIndex.getNumberOfComments() + 1);
        postIndexRepository.save(postIndex);
    }

    public List<CommentResponse> getCommentsForPost(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return CommentMapper.INSTANCE.toCommentResponseList(comments);
    }

    public CommentResponse getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BadRequestException("comment", "Comment not found with given id"));

        return CommentMapper.INSTANCE.toCommentResponse(comment);
    }

    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BadRequestException("comment", "Comment not found with given id"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("username", "Not found username in jwt token"));

        if (!comment.getCreatedBy().equals(user)) {
            throw new BadRequestException("comment", "Not access to delete comment for logged user");
        }

        commentRepository.delete(comment);
    }
}
