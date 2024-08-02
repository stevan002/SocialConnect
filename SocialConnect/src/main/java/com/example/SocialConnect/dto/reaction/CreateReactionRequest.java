package com.example.SocialConnect.dto.reaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReactionRequest {

    @NotBlank(message = "Reaction must have type")
    @Pattern(regexp = "LIKE|LOVE|DISLIKE", message = "Invalid reaction type")
    private String reactionType;
    private Long postId;
    private Long commentId;
}
