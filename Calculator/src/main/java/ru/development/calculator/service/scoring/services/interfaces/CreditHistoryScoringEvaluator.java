package ru.development.calculator.service.scoring.services.interfaces;

import ru.development.calculator.model.dto.ScoringDataDto;

import java.math.BigDecimal;

public interface CreditHistoryScoringEvaluator {
    BigDecimal evaluateCreditHistory(ScoringDataDto scoringDataDto);
}
