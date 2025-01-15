package ru.development.dossier.error_handler;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RequestProcessingException extends RuntimeException {
    public RequestProcessingException(String message) {
        super(message);
    }
}
