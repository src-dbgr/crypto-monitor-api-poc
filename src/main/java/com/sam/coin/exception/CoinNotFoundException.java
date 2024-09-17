package com.sam.coin.exception;

/**
 * Exception thrown when a requested coin is not found.
 */
public class CoinNotFoundException extends RuntimeException {
    public CoinNotFoundException(String message) {
        super(message);
    }
}