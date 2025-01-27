package ru.development.calculator.service.scoring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.development.calculator.model.dto.ScoringDataDto;
import ru.development.calculator.service.scoring.services.interfaces.ClientLoanBurdenEvaluator;

import java.math.BigDecimal;

@Service
@Slf4j
public class ClientLoanBurdenEvaluatorImpl implements ClientLoanBurdenEvaluator {
    @Override
    public BigDecimal evaluateCurrentLoadBurden(ScoringDataDto scoringDataDto) {
        //имитируем обращение к внешним базам для получения информации о текущей кредитной нагрузке клиента и расчета коэффициента ПДН
        BigDecimal score = BigDecimal.valueOf(50);
        log.debug("calculated evaluateCurrentLoadBurden, score = {}", score);
        return score;
    }
}
