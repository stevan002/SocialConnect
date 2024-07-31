package com.example.SocialConnect.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequest {

    @NotBlank(message = "Comment must have a text")
    private String text;
    @NotNull(message = "Comment must have a post")
    private Long postId;
}
