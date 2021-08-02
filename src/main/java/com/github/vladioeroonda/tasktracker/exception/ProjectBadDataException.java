package com.github.vladioeroonda.tasktracker.exception;

public class ProjectBadDataException extends RuntimeException{
    public ProjectBadDataException() {
    }

    public ProjectBadDataException(String message) {
        super(message);
    }

    public ProjectBadDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
