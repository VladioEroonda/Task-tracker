package com.github.vladioeroonda.tasktracker.exception;

public class TaskBadDataException extends RuntimeException {
    public TaskBadDataException() {
        super();
    }

    public TaskBadDataException(String message) {
        super(message);
    }

    public TaskBadDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
