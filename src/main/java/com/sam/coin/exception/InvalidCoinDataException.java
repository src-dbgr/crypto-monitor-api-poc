package com.sam.coin.exception;

/**
 * Exception thrown when invalid coin data is provided.
 */
public class InvalidCoinDataException extends RuntimeException {
    public InvalidCoinDataException(String message) {
        super(message);
    }
}