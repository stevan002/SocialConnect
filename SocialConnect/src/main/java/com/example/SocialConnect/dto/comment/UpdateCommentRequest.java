package com.example.SocialConnect.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCommentRequest {

    @NotBlank(message = "Comment must have a text")
    private String text;
}
