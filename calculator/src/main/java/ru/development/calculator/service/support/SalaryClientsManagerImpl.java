package ru.development.calculator.service.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.development.calculator.service.properties.CreditProperties;
import ru.development.calculator.service.support.interfaces.SalaryClientsManager;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalaryClientsManagerImpl implements SalaryClientsManager {
    private final CreditProperties creditProperties;

    @Override
    public BigDecimal getRateForSalaryClient(BigDecimal normalRate) {
        return normalRate.subtract(creditProperties.getRateDecreaseForSalaryClients());
    }
}
