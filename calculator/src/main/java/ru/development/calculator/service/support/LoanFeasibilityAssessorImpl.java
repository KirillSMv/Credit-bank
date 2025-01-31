package ru.development.calculator.service.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.development.calculator.error_handler.ScoringException;
import ru.development.calculator.service.properties.CreditProperties;
import ru.development.calculator.service.support.interfaces.LoanFeasibilityAssessor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanFeasibilityAssessorImpl implements LoanFeasibilityAssessor {
    private final CreditProperties creditProperties;

    @Override
    public void assessIfLoanFeasible(BigDecimal rate, BigDecimal amount, BigDecimal salary, int term) {
        BigDecimal maxAmountCanBePaid = salary.multiply(creditProperties.getMaxPercentOfSalaryForLoan()
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
                .multiply(BigDecimal.valueOf(term));
        if (maxAmountCanBePaid.compareTo(amount) < 0) {
            log.debug("метод assessIfLoanFeasible, отказ по причине несоответствия зарплаты, срока кредита и суммы." +
                    "Максимальная сумма возможная к выплате за указанный период = {}, общая сумма кредита = {}", maxAmountCanBePaid, amount);
            throw new ScoringException("К сожалению, вам отказано в кредите");
        }
    }
}
