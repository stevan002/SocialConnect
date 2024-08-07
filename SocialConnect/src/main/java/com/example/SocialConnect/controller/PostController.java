package com.example.SocialConnect.controller;

import com.example.SocialConnect.dto.http.ApiResponse;
import com.example.SocialConnect.dto.post.CreatePostRequest;
import com.example.SocialConnect.service.FileServiceMinio;
import com.example.SocialConnect.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final FileServiceMinio fileServiceMinio;

    @PostMapping(value = "/create-post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPost(@RequestPart("post") @Valid CreatePostRequest post, @RequestPart("file") MultipartFile file, Principal principal) {
        String username = principal.getName();
        postService.createPost(post, username, file);
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

    @GetMapping("/file/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {
        var minioResponse = fileServiceMinio.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, minioResponse.headers().get("Content-Disposition"))
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Path.of(filename)))
                .body(new InputStreamResource(minioResponse));
    }
}
