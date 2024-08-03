package com.tinkoff_lab;

import com.tinkoff_lab.exceptions.DatabaseConnectionException;
import com.tinkoff_lab.exceptions.DatabaseException;
import com.tinkoff_lab.exceptions.ExecutorServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(DatabaseException.class)
    public ResponseEntity<String> handleException(DatabaseException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DatabaseConnectionException.class)
    public ResponseEntity<String> handleException(DatabaseConnectionException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ExecutorServiceException.class)
    public ResponseEntity<String> handleException(ExecutorServiceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
