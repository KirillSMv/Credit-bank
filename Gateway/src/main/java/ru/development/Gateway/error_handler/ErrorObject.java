package ru.development.Gateway.error_handler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ErrorObject extends RuntimeException {
    private String status;
    private String message;
    private LocalDateTime timestamp;
}
