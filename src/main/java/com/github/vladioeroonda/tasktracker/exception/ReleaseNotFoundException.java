package com.github.vladioeroonda.tasktracker.exception;

public class ReleaseNotFoundException extends RuntimeException{

    public ReleaseNotFoundException(String message) {
        super(message);
    }
}
