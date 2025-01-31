package ru.development.calculator.model.dto.mapper;

import org.springframework.stereotype.Component;
import ru.development.calculator.model.CreditParameters;
import ru.development.calculator.model.dto.CreditDto;
import ru.development.calculator.model.dto.ExtraCreditParameters;

@Component
public class CreditDtoMapper {
    public CreditDto toCreditDto(CreditParameters creditParameters, ExtraCreditParameters extraCreditParameters) {
        return CreditDto.builder()
                .amount(creditParameters.getAmount())
                .term(creditParameters.getTerm())
                .monthlyPayment(creditParameters.getMonthlyPayment())
                .rate(creditParameters.getRate())
                .psk(creditParameters.getPsk())
                .paymentSchedule(creditParameters.getPaymentSchedule())
                .isInsuranceEnabled(extraCreditParameters.getIsInsuranceEnabled())
                .isSalaryClient(extraCreditParameters.getIsSalaryClient()).build();
    }
}
