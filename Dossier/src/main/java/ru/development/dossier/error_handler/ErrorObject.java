package ru.development.dossier.error_handler;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ErrorObject {
    private String status;
    private String message;
    private LocalDateTime timestamp;
}
