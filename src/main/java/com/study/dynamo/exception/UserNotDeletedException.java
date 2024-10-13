package com.study.dynamo.exception;


import org.springframework.http.HttpStatus;

public class UserNotDeletedException extends BaseException {
    public UserNotDeletedException(String errorTitle, String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorTitle, message);
    }
}
