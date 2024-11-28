package ru.development.calculator.service;

import ru.development.calculator.model.dto.CreditDto;
import ru.development.calculator.model.dto.LoanOfferDto;
import ru.development.calculator.model.dto.LoanStatementRequestFullDto;
import ru.development.calculator.model.dto.ScoringDataDto;

import java.util.List;

public interface CreditService {
    List<LoanOfferDto> calculateCreditConditions(LoanStatementRequestFullDto loanStatementRequestFullDto);

    CreditDto calculateCreditParameters(ScoringDataDto scoringDataDto);
}
