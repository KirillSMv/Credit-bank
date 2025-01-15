package ru.development.calculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditParametersShort {
    private BigDecimal amount;
    private BigDecimal rate;
    private Integer term;
}
