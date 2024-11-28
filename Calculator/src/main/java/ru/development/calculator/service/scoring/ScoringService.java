package ru.development.calculator.service.scoring;

import ru.development.calculator.model.dto.ScoringDataDto;

import java.math.BigDecimal;

public interface ScoringService {
    BigDecimal getScore(ScoringDataDto scoringDataDto);
}
