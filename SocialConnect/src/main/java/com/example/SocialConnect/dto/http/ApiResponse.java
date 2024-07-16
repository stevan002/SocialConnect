package com.example.SocialConnect.dto.http;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class ApiResponse {
    private final boolean success;
    private final String message;
}
