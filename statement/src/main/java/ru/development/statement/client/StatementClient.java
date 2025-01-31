package ru.development.statement.client;

import ru.development.statement.model.dto.LoanOfferDto;
import ru.development.statement.model.dto.LoanStatementRequestDto;

import java.util.List;

public interface StatementClient {
    List<LoanOfferDto> requestLoanOffers(LoanStatementRequestDto dto);

    void selectOffer(LoanOfferDto dto);

}
