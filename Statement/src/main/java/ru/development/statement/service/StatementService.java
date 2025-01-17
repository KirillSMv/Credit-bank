package ru.development.statement.service;

import ru.development.statement.model.dto.LoanOfferDto;
import ru.development.statement.model.dto.LoanStatementRequestDto;

import java.util.List;

public interface StatementService {

    List<LoanOfferDto> processLoanStatement(LoanStatementRequestDto dto);

    void selectOffer(LoanOfferDto dto);
}
