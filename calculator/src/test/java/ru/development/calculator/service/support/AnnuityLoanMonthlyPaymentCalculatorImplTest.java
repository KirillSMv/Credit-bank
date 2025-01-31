package ru.development.calculator.service.support;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.development.calculator.service.properties.CreditProperties;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ExtendWith(MockitoExtension.class)
class AnnuityLoanMonthlyPaymentCalculatorImplTest {
    @Mock
    private CreditProperties creditProperties;

    @InjectMocks
    private AnnuityLoanMonthlyPaymentCalculatorImpl annuityLoanMonthlyPaymentCalculator;

    @Test
    void getMonthlyPaymentTest_whenParametersPassed_thenAmountReturned() {
        Mockito.when(creditProperties.getInterCalcAccuracy()).thenReturn(10);
        BigDecimal expectedAmount = BigDecimal.valueOf(47765.0695000000).setScale(10, RoundingMode.HALF_UP);

        BigDecimal result = annuityLoanMonthlyPaymentCalculator.getMonthlyPayment(BigDecimal.valueOf(26),
                BigDecimal.valueOf(500000), 12);

        Assertions.assertEquals(expectedAmount, result);
    }
}