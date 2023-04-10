package com.sam.coin.exception;

public class UnknownEnumException extends IllegalArgumentException {
    public UnknownEnumException(String message) {
        super(message);
    }
}