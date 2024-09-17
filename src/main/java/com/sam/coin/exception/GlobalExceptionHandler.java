package com.sam.coin.exception;

import com.sam.coin.api.SamApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.dao.DataAccessException;

import jakarta.validation.ConstraintViolationException;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the Coin API.
 * Provides centralized exception handling across all @RequestMapping methods.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles CoinNotFoundException.
     *
     * @param ex The CoinNotFoundException.
     * @return ResponseEntity with error details.
     */
    @ExceptionHandler(CoinNotFoundException.class)
    public ResponseEntity<SamApiResponse<String>> handleCoinNotFoundException(CoinNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new SamApiResponse<>(false, null, ex.getMessage()));
    }

    /**
     * Handles InvalidCoinDataException.
     *
     * @param ex The InvalidCoinDataException.
     * @return ResponseEntity with error details.
     */
    @ExceptionHandler(InvalidCoinDataException.class)
    public ResponseEntity<SamApiResponse<String>> handleInvalidCoinDataException(InvalidCoinDataException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new SamApiResponse<>(false, null, ex.getMessage()));
    }

    /**
     * Handles DuplicateCoinException.
     *
     * @param ex The DuplicateCoinException.
     * @return ResponseEntity with error details.
     */
    @ExceptionHandler(DuplicateCoinException.class)
    public ResponseEntity<SamApiResponse<String>> handleDuplicateCoinException(DuplicateCoinException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new SamApiResponse<>(false, null, ex.getMessage()));
    }

    /**
     * Handles MethodArgumentNotValidException.
     *
     * @param ex The MethodArgumentNotValidException.
     * @return ResponseEntity with validation error details.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SamApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new SamApiResponse<>(false, errors, "Validation failed"));
    }

    /**
     * Handles ConstraintViolationException.
     *
     * @param ex The ConstraintViolationException.
     * @return ResponseEntity with constraint violation details.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<SamApiResponse<String>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new SamApiResponse<>(false, ex.getMessage(), "Constraint violation"));
    }

    /**
     * Handles DataAccessException.
     *
     * @param ex The DataAccessException.
     * @return ResponseEntity with database error details.
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<SamApiResponse<String>> handleDataAccessException(
            DataAccessException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new SamApiResponse<>(false, "An error occurred while accessing the database", "Database error"));
    }

    /**
     * Handles all other uncaught exceptions.
     *
     * @param ex The Exception.
     * @return ResponseEntity with generic error details.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<SamApiResponse<String>> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new SamApiResponse<>(false, "An unexpected error occurred", "Internal server error"));
    }
}