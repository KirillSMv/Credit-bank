package ru.development.deal.error_handler;

public class LoanRefusalException extends RuntimeException {
    public LoanRefusalException(String message) {
        super(message);
    }
}
