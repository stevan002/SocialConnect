package com.example.SocialConnect.dto.group;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GroupResponse {
    private String id;
    private String username;
    private String name;
    private String description;
    private LocalDateTime creationDate;
}
