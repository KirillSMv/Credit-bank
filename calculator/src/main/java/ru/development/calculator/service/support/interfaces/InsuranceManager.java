package ru.development.calculator.service.support.interfaces;

import java.math.BigDecimal;

public interface InsuranceManager {
    BigDecimal getRateWithInsurance(BigDecimal rate);

    BigDecimal getAmountWithInsurance(BigDecimal amount);
}
