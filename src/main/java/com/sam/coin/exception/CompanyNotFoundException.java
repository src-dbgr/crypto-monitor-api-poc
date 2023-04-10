package com.sam.coin.exception;

public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(String message, Throwable t) {
        super(message, t);
    }
}
