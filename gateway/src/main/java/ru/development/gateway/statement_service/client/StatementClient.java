package ru.development.gateway.statement_service.client;

import ru.development.gateway.model.dto.LoanOfferDto;
import ru.development.gateway.model.dto.LoanStatementRequestDto;

import java.util.List;

public interface StatementClient {
    List<LoanOfferDto> processLoanStatement(LoanStatementRequestDto dto);

    void selectOffer(LoanOfferDto dto);
}