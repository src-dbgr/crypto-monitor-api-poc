package com.sam.coin.exception;

/**
 * Exception thrown when attempting to add a duplicate coin.
 */
public class DuplicateCoinException extends RuntimeException {
    public DuplicateCoinException(String message) {
        super(message);
    }
}