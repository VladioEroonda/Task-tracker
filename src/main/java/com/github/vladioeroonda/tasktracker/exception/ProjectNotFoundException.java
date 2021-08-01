package com.github.vladioeroonda.tasktracker.exception;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException() {
    }

    public ProjectNotFoundException(String message) {
        super(message);
    }

    public ProjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
