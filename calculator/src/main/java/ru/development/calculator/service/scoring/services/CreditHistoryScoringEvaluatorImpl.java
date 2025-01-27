package ru.development.calculator.service.scoring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.development.calculator.model.dto.ScoringDataDto;
import ru.development.calculator.service.scoring.services.interfaces.CreditHistoryScoringEvaluator;

import java.math.BigDecimal;

@Service
@Slf4j
public class CreditHistoryScoringEvaluatorImpl implements CreditHistoryScoringEvaluator {
    @Override
    public BigDecimal evaluateCreditHistory(ScoringDataDto scoringDataDto) {
        //имитируем обращение к внешним базам для получения информации о кредитной истории
        BigDecimal score = BigDecimal.valueOf(50);
        log.debug("calculated evaluateCreditHistory, score = {}", score);
        return score;
    }
}
