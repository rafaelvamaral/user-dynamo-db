package com.study.dynamo.exception;


import org.springframework.http.HttpStatus;

public class UserNotPatchedException extends BaseException {
    public UserNotPatchedException(String errorTitle, String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorTitle, message);
    }
}
