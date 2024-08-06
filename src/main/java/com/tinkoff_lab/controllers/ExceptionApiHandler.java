package com.tinkoff_lab.controllers;

import com.tinkoff_lab.dao.TranslationDAO;
import com.tinkoff_lab.exceptions.DatabaseConnectionException;
import com.tinkoff_lab.exceptions.DatabaseException;
import com.tinkoff_lab.exceptions.ExecutorServiceException;
import com.tinkoff_lab.exceptions.TranslationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {     // global exception handler
    private TranslationDAO dao;

    @Autowired
    public ExceptionApiHandler(TranslationDAO dao) {
        this.dao = dao;
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<String> handleException(DatabaseException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DatabaseConnectionException.class)
    public ResponseEntity<String> handleException(DatabaseConnectionException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ExecutorServiceException.class)
    public ResponseEntity<String> handleException(ExecutorServiceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TranslationException.class)
    public ResponseEntity<String> handleException(TranslationException ex) {
        dao.insert(ex.getTranslation());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.valueOf(ex.getTranslation().status()));
    }
}
