package ru.development.calculator.service.credit_conditions;

import ru.development.calculator.model.dto.LoanOfferDto;
import ru.development.calculator.model.dto.LoanStatementRequestDto;

import java.util.List;

public interface CreditOffersService {
    List<LoanOfferDto> calculateCreditOffers(LoanStatementRequestDto loanStatementRequestDto);
}
