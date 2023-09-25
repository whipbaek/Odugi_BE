package com.example.communityservice.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private ErrorCode error;

    public ApiException(ErrorCode error) {
        super(error.getMessage());
        this.error = error;
    }

}
