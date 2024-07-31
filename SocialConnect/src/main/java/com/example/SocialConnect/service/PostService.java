package com.example.SocialConnect.service;

import com.example.SocialConnect.dto.group.GroupResponse;
import com.example.SocialConnect.dto.post.CreatePostRequest;
import com.example.SocialConnect.dto.post.PostResponse;
import com.example.SocialConnect.exception.BadRequestException;
import com.example.SocialConnect.mapper.GroupMapper;
import com.example.SocialConnect.mapper.PostMapper;
import com.example.SocialConnect.model.Group;
import com.example.SocialConnect.model.Post;
import com.example.SocialConnect.model.User;
import com.example.SocialConnect.repository.GroupRepository;
import com.example.SocialConnect.repository.PostRepository;
import com.example.SocialConnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public void createPost(CreatePostRequest post, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("username", "username not found in jwt token"));

        Group group = null;
        if (post.getGroupId() != null) {
            group = groupRepository.findById(post.getGroupId())
                    .orElseThrow(() -> new BadRequestException("groupId", "Group with given id not found"));
        }

        Post postEntity = Post.builder()
                .content(post.getContent())
                .createDate(LocalDateTime.now())
                .group(group)
                .postedBy(user)
                .build();

        postRepository.save(postEntity);
    }

    public List<PostResponse> getPosts() {
        List<Post> posts = postRepository.findAll();
        return PostMapper.INSTANCE.toPostResponseList(posts);
    }

    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException("post", "Not found post with given id"));

        return PostMapper.INSTANCE.toPostResponse(post);
    }

    public void deletePost(Long postId, String username){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException("post", "Not found post with given id"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("username", "Not found username in jwt token"));

        if (!post.getPostedBy().equals(user)) {
            throw new BadRequestException("group", "Not access to delete group for logged user");
        }
        postRepository.delete(post);
    }
}
