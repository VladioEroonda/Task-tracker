package com.github.vladioeroonda.tasktracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {
            ProjectNotFoundException.class,
            ReleaseNotFoundException.class,
            TaskNotFoundException.class,
            UserNotFoundException.class
    })

    public ResponseEntity<Object> handleNotFoundException(RuntimeException e) {
        ExceptionTemplate exceptionResponse = new ExceptionTemplate(e.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {
            TaskBadDataException.class,
            UserBadDataException.class,
            ReleaseBadDataException.class,
            ProjectBadDataException.class
    })
    public ResponseEntity<Object> handleBadDataException(RuntimeException e) {
        ExceptionTemplate exceptionResponse = new ExceptionTemplate(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ProjectClosingException.class, ReleaseClosingException.class})
    public ResponseEntity<Object> handleClosingException(RuntimeException e) {
        ExceptionTemplate exceptionResponse = new ExceptionTemplate(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
