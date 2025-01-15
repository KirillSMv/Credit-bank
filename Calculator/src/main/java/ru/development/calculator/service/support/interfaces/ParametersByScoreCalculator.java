package ru.development.calculator.service.support.interfaces;

import ru.development.calculator.model.CreditParametersShort;
import ru.development.calculator.model.dto.ScoringDataDto;

import java.math.BigDecimal;

public interface ParametersByScoreCalculator {

    CreditParametersShort getRateAndAmount(BigDecimal score, ScoringDataDto scoringDataDto);
}
