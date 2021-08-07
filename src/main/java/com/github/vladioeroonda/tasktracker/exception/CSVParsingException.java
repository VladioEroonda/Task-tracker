package com.github.vladioeroonda.tasktracker.exception;

public class CSVParsingException extends RuntimeException{
    public CSVParsingException() {
    }

    public CSVParsingException(String message) {
        super(message);
    }

    public CSVParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
