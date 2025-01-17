package ru.development.Deal.client;

import ru.development.Deal.model.dto.CreditDto;
import ru.development.Deal.model.dto.LoanOfferDto;
import ru.development.Deal.model.dto.LoanStatementRequestDto;
import ru.development.Deal.model.dto.ScoringDataDto;

import java.util.List;

public interface DealClient {
    List<LoanOfferDto> calculateOffers(LoanStatementRequestDto dto);

    CreditDto calculateCreditParameters(ScoringDataDto dto);
}
