package ru.development.calculator.model.dto;

import lombok.Data;

@Data
public class EmailMessage {
    private String address;
    private Enum theme;
    private Long statementId;
    private String text;
}
