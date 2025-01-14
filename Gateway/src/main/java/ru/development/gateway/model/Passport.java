package ru.development.gateway.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passport {
    private UUID passportIdUuid;
    private String series;
    private String number;
    private String issueBranch;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;
}
