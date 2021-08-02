package com.github.vladioeroonda.tasktracker.exception;

public class ReleaseBadDataException extends RuntimeException{
    public ReleaseBadDataException() {
    }

    public ReleaseBadDataException(String message) {
        super(message);
    }

    public ReleaseBadDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
