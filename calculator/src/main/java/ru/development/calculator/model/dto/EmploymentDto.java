package ru.development.calculator.model.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;
import ru.development.calculator.model.enums.EmploymentStatus;
import ru.development.calculator.model.enums.PositionType;
import ru.development.calculator.validation.custom_annotations.SalaryValidation;

import java.math.BigDecimal;

@Data
@Builder
public class EmploymentDto {
    private String employerINN;
    private EmploymentStatus employmentStatus;
    private BigDecimal salary;
    private PositionType positionType;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
