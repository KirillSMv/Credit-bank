package ru.development.statement.exceptions;

import lombok.Data;

@Data
public class ErrorObject {
    private String status;
    private String message;
    private String localDateTime;

    public ErrorObject(String status, String message, String localDateTime) {
        this.status = status;
        this.message = message;
        this.localDateTime = localDateTime;
    }
}
