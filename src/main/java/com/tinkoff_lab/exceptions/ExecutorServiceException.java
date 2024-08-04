package com.tinkoff_lab.exceptions;

public class ExecutorServiceException extends RuntimeException{
    public ExecutorServiceException(String message) {   // exception in case of troubles with threads
        super(message);
    }
}
