package ru.development.deal.service.interfaces;

import ru.development.deal.model.dto.FinishRegistrationRequestDto;
import ru.development.deal.model.dto.LoanOfferDto;
import ru.development.deal.model.dto.LoanStatementRequestDto;

import java.util.List;

public interface DealService {
    List<LoanOfferDto> processLoanStatement(LoanStatementRequestDto dto);

    void selectOffer(LoanOfferDto dto);

    void finalizeLoanParameters(FinishRegistrationRequestDto dto, String statementId);

    void sendDocuments(String statementId);

    void signDocuments(String statementId);

    void processSesCode(String statementId, String code);
}
