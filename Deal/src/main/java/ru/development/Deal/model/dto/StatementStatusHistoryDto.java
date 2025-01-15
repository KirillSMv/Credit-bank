package ru.development.Deal.model.dto;

import lombok.Data;
import ru.development.Deal.model.enums.ApplicationStatus;
import ru.development.Deal.model.enums.ChangeType;

import java.time.LocalDateTime;

@Data
public class StatementStatusHistoryDto {
    private ApplicationStatus status;
    private LocalDateTime time;
    private ChangeType changeType;
}
