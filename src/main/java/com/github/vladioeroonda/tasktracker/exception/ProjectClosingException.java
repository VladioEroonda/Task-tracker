package com.github.vladioeroonda.tasktracker.exception;

public class ProjectClosingException extends RuntimeException {
    public ProjectClosingException() {
    }

    public ProjectClosingException(String message) {
        super(message);
    }

    public ProjectClosingException(String message, Throwable cause) {
        super(message, cause);
    }
}
