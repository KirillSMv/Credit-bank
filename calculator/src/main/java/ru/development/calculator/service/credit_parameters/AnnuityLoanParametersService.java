package ru.development.calculator.service.credit_parameters;

import ru.development.calculator.model.CreditParameters;
import ru.development.calculator.model.CreditParametersShort;

public interface AnnuityLoanParametersService {
    CreditParameters calculateAnnuityLoanParameters(CreditParametersShort creditParametersShort);
}
