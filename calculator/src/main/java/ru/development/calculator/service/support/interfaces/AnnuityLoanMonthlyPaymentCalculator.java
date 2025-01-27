package ru.development.calculator.service.support.interfaces;

import java.math.BigDecimal;

public interface AnnuityLoanMonthlyPaymentCalculator {
    BigDecimal getMonthlyPayment(BigDecimal rate, BigDecimal amount, int term);
}
