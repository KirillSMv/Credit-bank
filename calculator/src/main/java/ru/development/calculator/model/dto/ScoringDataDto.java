package ru.development.calculator.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;
import ru.development.calculator.model.enums.Gender;
import ru.development.calculator.model.enums.MaritalStatus;
import ru.development.calculator.validation.custom_annotations.AccountNumberValidation;
import ru.development.calculator.validation.custom_annotations.AgeValidation;
import ru.development.calculator.validation.custom_annotations.MiddleNameValidation;
import ru.development.calculator.validation.custom_annotations.PassportDateValidation;

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
