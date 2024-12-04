package ru.development.calculator.service.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "credit-variables")
public class CreditProperties {
    private BigDecimal centralBankInterestRate;
    private BigDecimal averageRateAboveCentralBInterestRate;
    private BigDecimal averageInsuranceInPercentFromLoan;
    private BigDecimal rateDecreaseForSalaryClients;
    private BigDecimal rateDecreaseForInsurance;
    private Integer interCalcAccuracy;
    private Integer finalCalcAccuracy;
    private Integer firstRatingClass;
    private Integer secondRatingClass;
    private Integer thirdRatingClass;
    private Integer forthRatingClass;
    private Integer fifthRatingClass;
    private BigDecimal rateDifferenceSecondToThirdClass;
    private BigDecimal rateDifferenceThirdToForthClass;
    private BigDecimal rateDifferenceForthToFifthClass;
    private BigDecimal loanAmountSecondToThirdClass;
    private BigDecimal loanAmountThirdToForthClass;
    private BigDecimal loanAmountForthToFifthClass;
    private BigDecimal maxPercentOfSalaryForLoan;
}
