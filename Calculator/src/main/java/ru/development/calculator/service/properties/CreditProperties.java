package ru.development.calculator.service.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@Component
public class CreditProperties {
    @Value("${centralBankInterestRate}")
    private BigDecimal centralBankInterestRate;
    @Value("${averageRateAboveCentralBInterestRate}")
    private BigDecimal averageRateAboveCentralBInterestRate;
    @Value("${averageInsuranceInPercentFromLoan}")
    private BigDecimal averageInsuranceInPercentFromLoan;
    @Value("${rateDecreaseForSalaryClients}")
    private BigDecimal rateDecreaseForSalaryClients;
    @Value("${rateDecreaseForInsurance}")
    private BigDecimal rateDecreaseForInsurance;
    @Value("${interCalcAccuracy}")
    private Integer interCalcAccuracy;
    @Value("${finalCalcAccuracy}")
    private Integer finalCalcAccuracy;
    @Value("${paymentAccuracy}")
    private Integer paymentAccuracy;
    @Value("${scoreAccuracy}")
    private Integer scoreAccuracy;
    @Value("${firstRatingClass}")
    private Integer firstRatingClass;
    @Value("${secondRatingClass}")
    private Integer secondRatingClass;
    @Value("${thirdRatingClass}")
    private Integer thirdRatingClass;
    @Value("${forthRatingClass}")
    private Integer forthRatingClass;
    @Value("${fifthRatingClass}")
    private Integer fifthRatingClass;
    @Value("${rateDifferenceSecondToThirdClass}")
    private BigDecimal rateDifferenceSecondToThirdClass;
    @Value("${rateDifferenceThirdToForthClass}")
    private BigDecimal rateDifferenceThirdToForthClass;
    @Value("${rateDifferenceForthToFifthClass}")
    private BigDecimal rateDifferenceForthToFifthClass;
    @Value("${loanAmountSecondToThirdClass}")
    private BigDecimal loanAmountSecondToThirdClass;
    @Value("${loanAmountThirdToForthClass}")
    private BigDecimal loanAmountThirdToForthClass;
    @Value("${loanAmountForthToFifthClass}")
    private BigDecimal loanAmountForthToFifthClass;
    @Value("${maxPercentOfSalaryForLoan}")
    private BigDecimal maxPercentOfSalaryForLoan;
}
