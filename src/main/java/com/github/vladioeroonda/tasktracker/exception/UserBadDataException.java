package com.github.vladioeroonda.tasktracker.exception;

public class UserBadDataException extends RuntimeException {
    public UserBadDataException() {
    }

    public UserBadDataException(String message) {
        super(message);
    }

    public UserBadDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
