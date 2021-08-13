package com.github.vladioeroonda.tasktracker.exception;

public class ReleaseClosingException extends RuntimeException {
    public ReleaseClosingException() {
    }

    public ReleaseClosingException(String message) {
        super(message);
    }

    public ReleaseClosingException(String message, Throwable cause) {
        super(message, cause);
    }
}
