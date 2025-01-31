package ru.development.calculator.service.scoring.score_properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "loan-amount-score")
@Getter
@Setter
@Component
public class LoanAmountParametersProperties {
    private BigDecimal maxLoanToSalariesRatio;
    private BigDecimal lowLoanToSalariesRatio;
    private BigDecimal lowLoanToSalariesRatioCoef;
    private BigDecimal averageLoanToSalariesRatio;
    private BigDecimal averageLoanToSalariesRatioCoef;
    private BigDecimal otherLoanToSalariesRatioCoef;
}
