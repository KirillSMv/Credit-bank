package ru.development.calculator.service.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.development.calculator.service.properties.CreditProperties;
import ru.development.calculator.service.support.interfaces.AnnuityLoanMonthlyPaymentCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnnuityLoanMonthlyPaymentCalculatorImpl implements AnnuityLoanMonthlyPaymentCalculator {
    private final CreditProperties creditProperties;

    @Override
    public BigDecimal getMonthlyPayment(BigDecimal rate, BigDecimal amount, int term) {
        BigDecimal monthlyRateInPercent = rate.divide(BigDecimal.valueOf(12), creditProperties.getInterCalcAccuracy(), RoundingMode.HALF_UP);
        BigDecimal monthlyRate = monthlyRateInPercent.divide(BigDecimal.valueOf(100), creditProperties.getInterCalcAccuracy(), RoundingMode.HALF_UP);
        BigDecimal partOfFormulaCalculation = monthlyRate.add(BigDecimal.valueOf(1)).pow(term);
        BigDecimal annuityCoefficient = (monthlyRate.multiply(partOfFormulaCalculation))
                .divide(partOfFormulaCalculation.subtract(BigDecimal.valueOf(1)), creditProperties.getInterCalcAccuracy(), RoundingMode.HALF_UP);
        return amount.multiply(annuityCoefficient);
    }
}
