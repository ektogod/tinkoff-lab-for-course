package com.tinkoff_lab.exceptions;

public class DatabaseConnectionException extends RuntimeException{
    public DatabaseConnectionException(String message) {   // exception in case of troubles with connection to database
        super(message);
    }
}
