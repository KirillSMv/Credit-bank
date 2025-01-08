package ru.development.Dossier.error_handler;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ErrorObject {
    private String status;
    private String message;
    private LocalDateTime timestamp;
}
