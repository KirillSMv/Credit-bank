package ru.development.calculator.service.scoring.score_properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@Component
public class ScoringParametersRatioProperties {
    @Value("${employmentScoreRatio}")
    private BigDecimal employmentScoreRatio;
    @Value("${creditHistoryScoreRatio}")
    private BigDecimal creditHistoryScoreRatio;
    @Value("${clientLoanBurdenScoreRatio}")
    private BigDecimal clientLoanBurdenScoreRatio;
    @Value("${familyParametersScoreRatio}")
    private BigDecimal familyParametersScoreRatio;
    @Value("${loanAmountScoreRatio}")
    private BigDecimal loanAmountScoreRatio;
    @Value("${ageGenderScoreRatio}")
    private BigDecimal ageGenderScoreRatio;
}
