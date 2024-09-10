package com.sam.coin.exception;

public class InvalidCoinDataException extends RuntimeException {
    public InvalidCoinDataException(String message) {
        super(message);
    }
}