package ru.development.calculator.service.scoring.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.development.calculator.error_handler.ScoringException;
import ru.development.calculator.model.dto.ScoringDataDto;
import ru.development.calculator.service.scoring.score_properties.LoanAmountParametersProperties;
import ru.development.calculator.service.scoring.services.interfaces.LoanAmountEvaluator;
import ru.development.calculator.service.support.AnnuityLoanMonthlyPaymentCalculatorImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoanAmountEvaluatorImpl implements LoanAmountEvaluator {

    private final LoanAmountParametersProperties loanAmountParametersProperties;
    @Value("${scoreAccuracy}")
    private Integer scoreAccuracy;
    private AnnuityLoanMonthlyPaymentCalculatorImpl annuityLoanMonthlyPaymentCalculator;

    @Override
    public BigDecimal evaluateAmountOfLoan(ScoringDataDto scoringDataDto) {
        if (scoringDataDto.getAmount().compareTo(scoringDataDto.getEmployment().getSalary()
                .multiply(loanAmountParametersProperties.getMaxLoanToSalariesRatio())) > 0) {
            log.warn("метод evaluateAmountOfLoan, отказ по причине превышения суммы кредита: " +
                            "{} размеру заданного количества зарплат. Зарплата - {}, соотношение - {}",
                    scoringDataDto.getAmount(), scoringDataDto.getEmployment().getSalary(),
                    loanAmountParametersProperties.getMaxLoanToSalariesRatio());
            throw new ScoringException("К сожалению, вам отказано в кредите");
        }
        BigDecimal score;
        if (scoringDataDto.getAmount().compareTo(scoringDataDto.getEmployment().getSalary()
                .multiply(loanAmountParametersProperties.getLowLoanToSalariesRatio())) < 0) {
            score = BigDecimal.valueOf(100).multiply(loanAmountParametersProperties.getLowLoanToSalariesRatioCoef());
        } else if (scoringDataDto.getAmount().compareTo(scoringDataDto.getEmployment().getSalary()
                .multiply(loanAmountParametersProperties.getAverageLoanToSalariesRatio())) < 0) {
            score = BigDecimal.valueOf(100).multiply(loanAmountParametersProperties.getAverageLoanToSalariesRatioCoef());
        } else {
            score = BigDecimal.valueOf(100).multiply(loanAmountParametersProperties.getOtherLoanToSalariesRatioCoef());
        }
        log.debug("calculated evaluateAmountOfLoan, score = {}", score.setScale(0, RoundingMode.HALF_UP));
        return score.setScale(scoreAccuracy, RoundingMode.HALF_UP);
    }
}
