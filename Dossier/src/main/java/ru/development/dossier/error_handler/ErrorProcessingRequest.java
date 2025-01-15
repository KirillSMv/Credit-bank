package ru.development.dossier.error_handler;

public class ErrorProcessingRequest extends RuntimeException {
    public ErrorProcessingRequest(String message) {
        super(message);
    }
}
