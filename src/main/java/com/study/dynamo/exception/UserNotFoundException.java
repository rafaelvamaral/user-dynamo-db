package com.study.dynamo.exception;


import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String errorTitle, String message) {
        super(HttpStatus.NOT_FOUND.value(), errorTitle, message);
    }
}
