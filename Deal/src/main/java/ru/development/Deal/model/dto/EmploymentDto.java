package ru.development.Deal.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.development.Deal.model.enums.EmploymentStatus;
import ru.development.Deal.model.enums.PositionType;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentDto {
    private String employerINN;
    private EmploymentStatus employmentStatus;
    private BigDecimal salary;
    private PositionType positionType;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
