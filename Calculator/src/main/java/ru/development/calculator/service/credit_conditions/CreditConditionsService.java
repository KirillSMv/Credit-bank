package ru.development.calculator.service.credit_conditions;

import ru.development.calculator.model.dto.LoanOfferDto;
import ru.development.calculator.model.dto.LoanStatementRequestFullDto;

import java.util.List;

public interface CreditConditionsService {
    List<LoanOfferDto> calculateCreditConditions(LoanStatementRequestFullDto loanStatementRequestFullDto);
}
