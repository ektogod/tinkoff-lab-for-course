package com.tinkoff_lab.exceptions;

import lombok.Getter;

@Getter
public class ExecutorServiceException extends RuntimeException{  //exception in case of troubles with threads
    public ExecutorServiceException(String message) {   // exception in case of troubles with threads
        super(message);
    }
}
