package ru.development.Deal.service.interfaces;

import ru.development.Deal.model.dto.FinishRegistrationRequestDto;
import ru.development.Deal.model.dto.LoanOfferDto;
import ru.development.Deal.model.dto.LoanStatementRequestDto;

import java.util.List;

public interface DealService {
    List<LoanOfferDto> processLoanStatement(LoanStatementRequestDto dto);

    void selectOffer(LoanOfferDto dto);

    void finalizeLoanParameters(FinishRegistrationRequestDto dto, String statementId);

    void sendDocuments(String statementId);

    void signDocuments(String statementId);

    void processSesCode(String statementId, String code);
}
