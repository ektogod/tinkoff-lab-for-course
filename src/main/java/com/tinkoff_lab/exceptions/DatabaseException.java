package com.tinkoff_lab.exceptions;

public class DatabaseException extends RuntimeException{
    public DatabaseException(String message) {  // exception in case of troubles with saving data in database
        super(message);
    }
}
