package com.study.dynamo.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    private final int httpStatus;

    private final String errorTitle;

    public BaseException(int httpStatus, String errorTitle, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorTitle = errorTitle;
    }
}
