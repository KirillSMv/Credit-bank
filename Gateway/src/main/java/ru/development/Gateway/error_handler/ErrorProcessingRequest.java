package ru.development.Gateway.error_handler;

public class ErrorProcessingRequest extends RuntimeException {
    public ErrorProcessingRequest(String message) {
        super(message);
    }
}
