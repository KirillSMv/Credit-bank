package ru.development.calculator.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.development.calculator.model.enums.Gender;
import ru.development.calculator.model.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ScoringDataDto {
    private BigDecimal amount;
    private Integer term;
    private String firstName;
    private String lastName;
    private String middleName;
    private Gender gender;
    private EmploymentDto employment;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private Integer dependentAmount;
    private String accountNumber;
    private MaritalStatus maritalStatus;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}
