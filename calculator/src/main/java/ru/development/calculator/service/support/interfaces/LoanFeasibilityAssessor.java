package ru.development.calculator.service.support.interfaces;

import java.math.BigDecimal;

public interface LoanFeasibilityAssessor {
    void assessIfLoanFeasible(BigDecimal rate, BigDecimal amount, BigDecimal salary, int term);
}
