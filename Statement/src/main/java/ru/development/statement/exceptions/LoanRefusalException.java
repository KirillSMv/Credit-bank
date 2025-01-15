package ru.development.statement.exceptions;

public class LoanRefusalException extends RuntimeException {
    public LoanRefusalException(String message) {
        super(message);
    }
}
