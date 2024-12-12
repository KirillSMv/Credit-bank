package ru.development.calculator.service;

import ru.development.calculator.model.dto.CreditDto;
import ru.development.calculator.model.dto.LoanOfferDto;
import ru.development.calculator.model.dto.LoanStatementRequestDto;
import ru.development.calculator.model.dto.ScoringDataDto;

import java.util.List;

public interface CreditService {
    List<LoanOfferDto> calculateCreditConditions(LoanStatementRequestDto loanStatementRequestDto);

    CreditDto calculateCreditParameters(ScoringDataDto scoringDataDto);
}
