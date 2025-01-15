package ru.development.calculator.model.dto;

import lombok.Data;
import ru.development.calculator.model.enums.Gender;
import ru.development.calculator.model.enums.MaritalStatus;

import java.time.LocalDate;

@Data
public class FinishRegistrationRequestDto {
    private Gender gender;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private EmploymentDto employmentDto;
    private String accountNumber;
}
