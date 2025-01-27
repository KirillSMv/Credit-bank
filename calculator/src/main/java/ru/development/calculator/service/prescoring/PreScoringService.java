package ru.development.calculator.service.prescoring;

import ru.development.calculator.model.dto.LoanStatementRequestDto;

public interface PreScoringService {

    void preScoring(LoanStatementRequestDto loanStatementRequestDto);
}
