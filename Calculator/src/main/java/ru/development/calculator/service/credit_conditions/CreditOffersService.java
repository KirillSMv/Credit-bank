package ru.development.calculator.service.credit_conditions;

import ru.development.calculator.model.dto.LoanOfferDto;
import ru.development.calculator.model.dto.LoanStatementRequestFullDto;

import java.util.List;

public interface CreditOffersService {
    List<LoanOfferDto> calculateCreditOffers(LoanStatementRequestFullDto loanStatementRequestFullDto);
}
