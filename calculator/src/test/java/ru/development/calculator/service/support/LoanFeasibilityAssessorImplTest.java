package ru.development.calculator.service.support;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.development.calculator.error_handler.ScoringException;
import ru.development.calculator.service.properties.CreditProperties;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class LoanFeasibilityAssessorImplTest {
    @Mock
    private CreditProperties creditProperties;
    @InjectMocks
    private LoanFeasibilityAssessorImpl loanFeasibilityAssessor;


    @Test
    void assessIfLoanFeasibleTest_whenLoanCannotBePaid_thenThrowScoringException() {
        Mockito.when(creditProperties.getMaxPercentOfSalaryForLoan()).thenReturn(BigDecimal.valueOf(50));
        Assertions.assertThrows(ScoringException.class, () -> loanFeasibilityAssessor.assessIfLoanFeasible(BigDecimal.valueOf(25), BigDecimal.valueOf(200000),
                BigDecimal.valueOf(30000), 6));
    }
}