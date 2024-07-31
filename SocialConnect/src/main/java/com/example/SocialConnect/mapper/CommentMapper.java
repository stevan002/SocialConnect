package com.example.SocialConnect.mapper;

import com.example.SocialConnect.dto.comment.CommentResponse;
import com.example.SocialConnect.dto.post.PostResponse;
import com.example.SocialConnect.model.Comment;
import com.example.SocialConnect.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "createdBy.username", target = "username")
    CommentResponse toCommentResponse(Comment comment);

    List<CommentResponse> toCommentResponseList(List<Comment> comments);
}
