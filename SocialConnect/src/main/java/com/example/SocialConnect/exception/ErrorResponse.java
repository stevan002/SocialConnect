package com.example.SocialConnect.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String fieldName;
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

}
