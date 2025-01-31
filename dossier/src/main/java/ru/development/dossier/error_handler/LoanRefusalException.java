package ru.development.dossier.error_handler;

public class LoanRefusalException extends RuntimeException {
    public LoanRefusalException(String message) {
        super(message);
    }
}
