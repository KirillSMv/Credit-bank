package ru.development.Gateway.statement_service.client;

import ru.development.Gateway.statement_service.model.LoanOfferDto;
import ru.development.Gateway.statement_service.model.LoanStatementRequestDto;

import java.util.List;

public interface StatementClient {
    List<LoanOfferDto> processLoanStatement(LoanStatementRequestDto dto);
    void selectOffer(LoanOfferDto dto);
}