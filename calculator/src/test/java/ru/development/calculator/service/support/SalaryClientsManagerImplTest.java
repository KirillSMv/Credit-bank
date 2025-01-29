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

@ExtendWith(MockitoExtension.class)
class SalaryClientsManagerImplTest {
    @Mock
    private CreditProperties creditProperties;

    @InjectMocks
    private SalaryClientsManagerImpl salaryClientsManager;

    @Test
    void getRateForSalaryClientTest_whenRateIsPassed_RateDecreased() {
        Mockito.when(creditProperties.getRateDecreaseForSalaryClients()).thenReturn(BigDecimal.valueOf(1));
        BigDecimal expectedRate = BigDecimal.valueOf(19);
        BigDecimal result = salaryClientsManager.getRateForSalaryClient(BigDecimal.valueOf(20));
        Assertions.assertEquals(expectedRate, result);
    }
}