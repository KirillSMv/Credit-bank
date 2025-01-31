package ru.development.deal.client.interfaces;

import ru.development.deal.model.dto.CreditDto;
import ru.development.deal.model.dto.LoanOfferDto;
import ru.development.deal.model.dto.LoanStatementRequestDto;
import ru.development.deal.model.dto.ScoringDataDto;

import java.util.List;

public interface DealClient {
    List<LoanOfferDto> calculateOffers(LoanStatementRequestDto dto);

    CreditDto calculateCreditParameters(ScoringDataDto dto);
}
