package ru.development.calculator.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.development.calculator.model.enums.EmploymentStatus;
import ru.development.calculator.model.enums.PositionType;

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
