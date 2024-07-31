package com.example.SocialConnect.mapper;

import com.example.SocialConnect.dto.post.PostResponse;
import com.example.SocialConnect.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(source = "postedBy.username", target = "username")
    @Mapping(source = "group.name", target = "groupName")
    PostResponse toPostResponse(Post post);

    List<PostResponse> toPostResponseList(List<Post> posts);
}
