package ru.development.gateway.error_handler;

public class LoanRefusalException extends RuntimeException {
    public LoanRefusalException(String message) {
        super(message);
    }
}
