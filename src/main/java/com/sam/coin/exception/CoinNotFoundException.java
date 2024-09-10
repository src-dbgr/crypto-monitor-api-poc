package com.sam.coin.exception;

public class CoinNotFoundException extends RuntimeException {
    public CoinNotFoundException(String message) {
        super(message);
    }
}