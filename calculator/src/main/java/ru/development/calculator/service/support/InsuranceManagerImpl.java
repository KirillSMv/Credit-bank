package ru.development.calculator.service.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.development.calculator.service.properties.CreditProperties;
import ru.development.calculator.service.support.interfaces.InsuranceManager;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
@RequiredArgsConstructor
public class InsuranceManagerImpl implements InsuranceManager {

    private final CreditProperties creditProperties;

    @Override
    public BigDecimal getRateWithInsurance(BigDecimal rate) {
        return rate.subtract(creditProperties.getRateDecreaseForInsurance());
    }

    @Override
    public BigDecimal getAmountWithInsurance(BigDecimal amount) {
        return amount.add(amount.multiply(creditProperties.getAverageInsuranceInPercentFromLoan()
                .divide(BigDecimal.valueOf(100), creditProperties.getFinalCalcAccuracy(), RoundingMode.HALF_UP)));
    }
}
