package ru.development.calculator.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StatementStatusHistoryDto {
    private Enum status;
    private LocalDateTime time;
    private Enum changeType;
}
