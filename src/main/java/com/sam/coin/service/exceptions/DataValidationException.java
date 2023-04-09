package com.sam.coin.service.exceptions;

public class DataValidationException extends RuntimeException {
    public DataValidationException(String message) {
        super(message);
    }
}