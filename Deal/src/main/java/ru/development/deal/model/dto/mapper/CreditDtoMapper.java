package ru.development.deal.model.dto.mapper;

import org.springframework.stereotype.Component;
import ru.development.deal.model.Credit;
import ru.development.deal.model.dto.CreditDto;
import ru.development.deal.model.enums.CreditStatus;

@Component
public class CreditDtoMapper {
    public Credit toCredit(CreditDto creditDto) {
        return Credit.builder()
                .amount(creditDto.getAmount())
                .term(creditDto.getTerm())
                .monthlyPayment(creditDto.getMonthlyPayment())
                .rate(creditDto.getRate())
                .psk(creditDto.getPsk())
                .paymentSchedule(creditDto.getPaymentSchedule())
                .insuranceEnabled(creditDto.getIsInsuranceEnabled())
                .salaryClient(creditDto.getIsSalaryClient())
                .creditStatus(CreditStatus.CALCULATED)
                .build();
    }
}
