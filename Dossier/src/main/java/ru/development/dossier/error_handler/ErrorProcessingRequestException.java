package ru.development.dossier.error_handler;

public class ErrorProcessingRequestException extends RuntimeException {
    public ErrorProcessingRequestException(String message) {
        super(message);
    }
}
