package com.example.SocialConnect.mapper;

import com.example.SocialConnect.dto.comment.CommentResponse;
import com.example.SocialConnect.model.Comment;
import com.example.SocialConnect.model.ReactionType;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "createdBy.username", target = "username")
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "loveCount", ignore = true)
    @Mapping(target = "dislikeCount", ignore = true)
    CommentResponse toCommentResponse(Comment comment);

    List<CommentResponse> toCommentResponseList(List<Comment> comments);

    @AfterMapping
    default void countReactions(Comment comment, @MappingTarget CommentResponse response) {
        response.setLikeCount(comment.getReactions().stream()
                .filter(reaction -> reaction.getType() == ReactionType.LIKE)
                .count());
        response.setLoveCount(comment.getReactions().stream()
                .filter(reaction -> reaction.getType() == ReactionType.LOVE)
                .count());
        response.setDislikeCount(comment.getReactions().stream()
                .filter(reaction -> reaction.getType() == ReactionType.DISLIKE)
                .count());
    }
}
