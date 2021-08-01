package com.github.vladioeroonda.tasktracker.exception;

public class ReleaseNotFoundException extends RuntimeException {
    public ReleaseNotFoundException() {
    }

    public ReleaseNotFoundException(String message) {
        super(message);
    }

    public ReleaseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
