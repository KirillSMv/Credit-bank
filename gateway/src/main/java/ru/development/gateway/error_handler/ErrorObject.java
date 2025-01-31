package ru.development.gateway.error_handler;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ErrorObject {
    private String status;
    private LocalDateTime timestamp;
    private String message;

    public ErrorObject(String status, String message, LocalDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }
}

