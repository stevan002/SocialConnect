package com.example.SocialConnect.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String username;
    private String groupName;
    private LocalDateTime createDate;
    private long likeCount;
    private long loveCount;
    private long dislikeCount;
}
