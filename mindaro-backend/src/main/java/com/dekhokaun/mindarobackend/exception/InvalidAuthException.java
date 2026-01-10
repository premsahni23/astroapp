package com.dekhokaun.mindarobackend.exception;

import org.springframework.http.HttpStatus;

public class InvalidAuthException extends ApiException {
    public InvalidAuthException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
