package ru.development.Deal.service;

import ru.development.Deal.model.dto.FinishRegistrationRequestDto;
import ru.development.Deal.model.dto.LoanOfferDto;
import ru.development.Deal.model.dto.LoanStatementRequestDto;

import java.util.List;

public interface DealService {
    List<LoanOfferDto> processLoanStatement(LoanStatementRequestDto dto);

    void selectOffer(LoanOfferDto dto);

    void finalizeLoanParameters(FinishRegistrationRequestDto dto, String statementId);
}
