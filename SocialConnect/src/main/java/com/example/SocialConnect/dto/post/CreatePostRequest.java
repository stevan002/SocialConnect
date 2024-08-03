package com.example.SocialConnect.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostRequest {

    @NotBlank(message = "Content is required")
    @Size(min = 4, message = "Post content must have minimum 4 characters")
    private String content;

    private Long groupId;
}
