package ru.development.statement.exceptions;

public class ErrorProcessingRequest extends RuntimeException {
    public ErrorProcessingRequest(String message) {
        super(message);
    }
}
