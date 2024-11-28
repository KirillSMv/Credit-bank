package ru.development.calculator.service.prescoring;

import ru.development.calculator.model.dto.LoanStatementRequestFullDto;

public interface PreScoringService {

    void preScoring(LoanStatementRequestFullDto loanStatementRequestFullDto);
}
