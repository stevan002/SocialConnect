package com.example.SocialConnect.mapper;

import com.example.SocialConnect.dto.post.PostResponse;
import com.example.SocialConnect.model.Post;
import com.example.SocialConnect.model.ReactionType;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(source = "postedBy.username", target = "username")
    @Mapping(source = "group.name", target = "groupName")
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "loveCount", ignore = true)
    @Mapping(target = "dislikeCount", ignore = true)
    PostResponse toPostResponse(Post post);

    List<PostResponse> toPostResponseList(List<Post> posts);

    @AfterMapping
    default void setReactionCounts(Post post, @MappingTarget PostResponse response) {
        response.setLikeCount(post.getReactions().stream().filter(r -> r.getType() == ReactionType.LIKE).count());
        response.setLoveCount(post.getReactions().stream().filter(r -> r.getType() == ReactionType.LOVE).count());
        response.setDislikeCount(post.getReactions().stream().filter(r -> r.getType() == ReactionType.DISLIKE).count());
    }
}
