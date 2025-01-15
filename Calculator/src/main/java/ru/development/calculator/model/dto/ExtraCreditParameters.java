package ru.development.calculator.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExtraCreditParameters {
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}
