package com.sam.coin.exception;

public class DuplicateCoinException extends RuntimeException {
    public DuplicateCoinException(String message) {
        super(message);
    }
}