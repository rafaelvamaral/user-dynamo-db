package com.study.dynamo.exception;


import org.springframework.http.HttpStatus;

public class UserNotCreatedException extends BaseException {
    public UserNotCreatedException(String errorTitle, String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorTitle, message);
    }
}
