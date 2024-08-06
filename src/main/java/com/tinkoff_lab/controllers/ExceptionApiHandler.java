package com.tinkoff_lab.controllers;

import com.tinkoff_lab.dao.TranslationDAO;
import com.tinkoff_lab.dto.responses.ErrorResponse;
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
    private final TranslationDAO dao;

    @Autowired
    public ExceptionApiHandler(TranslationDAO dao) {
        this.dao = dao;
    }

    @ExceptionHandler({ DatabaseException.class, DatabaseConnectionException.class, ExecutorServiceException.class })
    public ResponseEntity<ErrorResponse> handleException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TranslationException.class)
    public ResponseEntity<ErrorResponse> handleException(TranslationException ex) {
        dao.insert(ex.getTranslation());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getTranslation().status());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getTranslation().status()));
    }
}
