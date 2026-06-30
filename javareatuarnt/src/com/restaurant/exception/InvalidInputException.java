package com.restaurant.exception;

/**
 * Custom exception for handling invalid inputs across the application.
 */
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}
