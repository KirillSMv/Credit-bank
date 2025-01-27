package ru.development.calculator.service.support.interfaces;

import java.math.BigDecimal;

public interface SalaryClientsManager {
    BigDecimal getRateForSalaryClient(BigDecimal normalRate);
}
