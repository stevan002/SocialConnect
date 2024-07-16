package com.example.SocialConnect.exception;

public class BadRequestException extends RuntimeException {
    private final String fieldName;
    public BadRequestException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }
    public String getFieldName() {
        return fieldName;
    }
}