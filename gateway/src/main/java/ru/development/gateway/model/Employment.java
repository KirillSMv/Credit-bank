package ru.development.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.development.gateway.model.enums.EmploymentStatus;
import ru.development.gateway.model.enums.PositionType;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Employment {
    private UUID employmentUuid;
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private PositionType position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
