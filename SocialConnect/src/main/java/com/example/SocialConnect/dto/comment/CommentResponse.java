package com.example.SocialConnect.dto.comment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {

    private String username;
    private String text;
    private LocalDateTime timestamp;
}
