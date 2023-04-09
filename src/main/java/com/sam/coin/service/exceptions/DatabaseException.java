package com.sam.coin.service.exceptions;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message, Throwable throwable) {
        super(message, throwable);
    }
}