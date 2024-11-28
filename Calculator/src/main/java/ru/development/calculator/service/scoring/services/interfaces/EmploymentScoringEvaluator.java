package ru.development.calculator.service.scoring.services.interfaces;

import ru.development.calculator.model.dto.EmploymentDto;

import java.math.BigDecimal;

public interface EmploymentScoringEvaluator {

    BigDecimal evaluateEmploymentData(EmploymentDto employmentDto);
}
