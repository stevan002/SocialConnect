package com.example.SocialConnect.service;

import com.example.SocialConnect.dto.group.GroupResponse;
import com.example.SocialConnect.dto.post.CreatePostRequest;
import com.example.SocialConnect.dto.post.PostResponse;
import com.example.SocialConnect.dto.post.UpdatePostRequest;
import com.example.SocialConnect.exception.BadRequestException;
import com.example.SocialConnect.indexmodel.GroupIndex;
import com.example.SocialConnect.indexmodel.PostIndex;
import com.example.SocialConnect.indexrepository.GroupIndexRepository;
import com.example.SocialConnect.indexrepository.PostIndexRepository;
import com.example.SocialConnect.mapper.GroupMapper;
import com.example.SocialConnect.mapper.PostMapper;
import com.example.SocialConnect.model.Group;
import com.example.SocialConnect.model.Post;
import com.example.SocialConnect.model.User;
import com.example.SocialConnect.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final FileServiceMinio fileService;
    private final PostIndexRepository postIndexRepository;
    private final GroupIndexRepository groupIndexRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;

    public void createPost(CreatePostRequest post, String username, MultipartFile file) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("username", "username not found in jwt token"));

        Group group = null;
        if (post.getGroupId() != null) {
            group = groupRepository.findById(post.getGroupId())
                    .orElseThrow(() -> new BadRequestException("groupId", "Group with given id not found"));
        }

        String filename = fileService.store(file, UUID.randomUUID().toString());

        Post postEntity = Post.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .createDate(LocalDateTime.now())
                .group(group)
                .postedBy(user)
                .contentFilename(filename)
                .build();

        postRepository.save(postEntity);

        if (post.getGroupId() != null) {
            GroupIndex groupIndex = groupIndexRepository.findByDatabaseId(
                    post.getGroupId()).orElse(null);

            if (groupIndex == null) {
                System.out.println("Index not found for group containing post");
            } else {
                long currentPosts = groupIndex.getNumberOfPosts();
                System.out.println("Current number of posts: " + currentPosts);
                groupIndex.setNumberOfPosts(currentPosts + 1);
                groupIndexRepository.save(groupIndex);
                System.out.println("Updated number of posts: " + groupIndex.getNumberOfPosts());
            }
        }

        PostIndex index = PostIndex.builder()
                .title(post.getTitle())
                .fullContent(post.getContent())
                .fileContent(extractDocumentContent(file))
                .numberOfLikes(0L)
                .numberOfComments(0L)
                .databaseId(postEntity.getId())
                .build();

        postIndexRepository.save(index);
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

        postIndexRepository.deleteByDatabaseId(post.getId());
        commentRepository.deleteAllByPostId(post.getId());
        reactionRepository.deleteAllByPostId(post.getId());
        postRepository.delete(post);
    }

    private String extractDocumentContent(MultipartFile multipartPdfFile) {
        String documentContent;
        try (InputStream pdfFile = multipartPdfFile.getInputStream()) {
            PDDocument pdDocument = PDDocument.load(pdfFile);
            PDFTextStripper textStripper = new PDFTextStripper();
            documentContent = textStripper.getText(pdDocument);
            pdDocument.close();
        } catch (IOException e) {
            throw new BadRequestException("pdf file", "Error while trying to load PDF file content for post");
        }

        return documentContent;
    }

    public void updatePost(Long postId, String username, UpdatePostRequest request) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException("post", "Not found post with given id"));

        if(!post.getPostedBy().getUsername().equals(username)) {
            throw new BadRequestException("post", "Not access to update post");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        postRepository.save(post);

        PostIndex index = postIndexRepository.findByDatabaseId(post.getId())
                .orElseThrow(() -> new BadRequestException("post", "Not found post index with given database id"));

        index.setTitle(request.getTitle());
        index.setFullContent(request.getContent());
        postIndexRepository.save(index);
    }
}
