package ru.development.calculator.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;
import ru.development.calculator.validation.custom_annotations.AgeValidation;
import ru.development.calculator.validation.custom_annotations.MiddleNameValidation;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class LoanStatementRequestDto {
    private BigDecimal amount;
    private Integer term;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthdate;
    private String email;
    private String passportSeries;
    private String passportNumber;
}
