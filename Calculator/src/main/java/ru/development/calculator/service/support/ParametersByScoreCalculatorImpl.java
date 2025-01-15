package ru.development.calculator.service.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.development.calculator.error_handler.ScoringException;
import ru.development.calculator.model.CreditParametersShort;
import ru.development.calculator.model.dto.EmploymentDto;
import ru.development.calculator.model.dto.ScoringDataDto;
import ru.development.calculator.service.properties.CreditProperties;
import ru.development.calculator.service.support.interfaces.ParametersByScoreCalculator;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParametersByScoreCalculatorImpl implements ParametersByScoreCalculator {
    private final CreditProperties creditProperties;

    @Override
    public CreditParametersShort getRateAndAmount(BigDecimal score, ScoringDataDto scoringDataDto) {
        log.debug("метод getRateAndAmount, рейтинг = {}", score);
        int intScore = score.intValue();
        CreditParametersShort creditParametersShort = new CreditParametersShort();
        EmploymentDto employmentDto = scoringDataDto.getEmployment();
        BigDecimal loanAmount = scoringDataDto.getAmount();
        if (intScore > creditProperties.getFirstRatingClass() && intScore < creditProperties.getSecondRatingClass()) {
            log.debug("оценка - низкая кредитоспособность. Клиент не набрал необходимый балл по рейтингу скоринга, рейтинг - {}",
                    intScore);
            throw new ScoringException("К сожалению, вам отказано в кредите");
        } else if (intScore > creditProperties.getSecondRatingClass() && intScore <= creditProperties.getThirdRatingClass()) {
            log.debug("оценка - средняя кредитоспособность, рейтинг = {}", intScore);
            BigDecimal maxAmount = employmentDto.getSalary().multiply(creditProperties.getLoanAmountSecondToThirdClass());
            creditParametersShort.setAmount(loanAmount.compareTo(maxAmount) >= 0 ? maxAmount : loanAmount);
            creditParametersShort.setRate(getSecondToThirdClassRate());
        } else if (intScore > creditProperties.getThirdRatingClass() && intScore <= creditProperties.getForthRatingClass()) {
            log.debug("оценка - хорошая кредитоспособность, рейтинг = {}", intScore);
            BigDecimal maxAmount = employmentDto.getSalary().multiply(creditProperties.getLoanAmountThirdToForthClass());
            creditParametersShort.setAmount(loanAmount.compareTo(maxAmount) >= 0 ? maxAmount : loanAmount);
            creditParametersShort.setRate(getThirdToForthClassRate());
        } else if (intScore > creditProperties.getForthRatingClass() && intScore <= creditProperties.getFifthRatingClass()) {
            log.debug("оценка - высокая кредитоспособность, рейтинг = {}", intScore);
            BigDecimal maxAmount = employmentDto.getSalary().multiply(creditProperties.getLoanAmountForthToFifthClass());
            creditParametersShort.setAmount(loanAmount.compareTo(maxAmount) >= 0 ? maxAmount : loanAmount);
            creditParametersShort.setRate(getForthToFifthClassRate());
        }
        log.debug("creditParametersShort: amount = {}, rate = {}", creditParametersShort.getAmount(),
                creditParametersShort.getRate());
        return creditParametersShort;
    }

    private BigDecimal getSecondToThirdClassRate() {
        return creditProperties.getCentralBankInterestRate().add(creditProperties.getAverageRateAboveCentralBInterestRate())
                .add(creditProperties.getRateDifferenceSecondToThirdClass());
    }

    private BigDecimal getThirdToForthClassRate() {
        return creditProperties.getCentralBankInterestRate().add(creditProperties.getAverageRateAboveCentralBInterestRate())
                .add(creditProperties.getRateDifferenceThirdToForthClass());
    }

    private BigDecimal getForthToFifthClassRate() {
        return creditProperties.getCentralBankInterestRate()
                .add(creditProperties.getAverageRateAboveCentralBInterestRate())
                .subtract(creditProperties.getRateDifferenceForthToFifthClass());
    }
}
