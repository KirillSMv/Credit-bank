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
class InsuranceManagerImplTest {
    @Mock
    private CreditProperties creditProperties;

    @InjectMocks
    private InsuranceManagerImpl insuranceManager;

    @Test
    void getRateWithInsuranceTest() {
        Mockito.when(creditProperties.getRateDecreaseForInsurance()).thenReturn(BigDecimal.valueOf(1));
        BigDecimal expectedRate = BigDecimal.valueOf(19);

        BigDecimal resultRate = insuranceManager.getRateWithInsurance(BigDecimal.valueOf(20));

        Assertions.assertEquals(expectedRate, resultRate);
    }

    @Test
    void getAmountWithInsuranceTest() {
        Mockito.when(creditProperties.getAverageInsuranceInPercentFromLoan()).thenReturn(BigDecimal.valueOf(1.5));
        Mockito.when(creditProperties.getFinalCalcAccuracy()).thenReturn(2);
        BigDecimal expectedAmount = BigDecimal.valueOf(510000).setScale(2, RoundingMode.HALF_UP);

        BigDecimal resultAmount = insuranceManager.getAmountWithInsurance(BigDecimal.valueOf(500000));
        Assertions.assertEquals(expectedAmount, resultAmount);
    }
}