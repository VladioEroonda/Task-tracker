package com.github.vladioeroonda.tasktracker.exception;

public class WrongFileTypeException extends RuntimeException{
    public WrongFileTypeException() {
        super();
    }

    public WrongFileTypeException(String message) {
        super(message);
    }

    public WrongFileTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
