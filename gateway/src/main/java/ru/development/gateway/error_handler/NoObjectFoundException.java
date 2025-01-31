package ru.development.gateway.error_handler;

public class NoObjectFoundException extends RuntimeException {
    public NoObjectFoundException(String message) {
        super(message);
    }
}
