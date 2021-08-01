package com.github.vladioeroonda.tasktracker.exception;

import org.springframework.http.HttpStatus;

public class ExceptionTemplate {
    private final String message;
    private final HttpStatus httpStatus;

    public ExceptionTemplate(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
