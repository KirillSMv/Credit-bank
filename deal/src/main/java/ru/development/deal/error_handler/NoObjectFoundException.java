package ru.development.deal.error_handler;

public class NoObjectFoundException extends RuntimeException {
    public NoObjectFoundException(String message) {
        super(message);
    }
}
