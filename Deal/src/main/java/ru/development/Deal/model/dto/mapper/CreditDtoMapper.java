package ru.development.Deal.model.dto.mapper;

import org.springframework.stereotype.Component;
import ru.development.Deal.model.Credit;
import ru.development.Deal.model.dto.CreditDto;
import ru.development.Deal.model.enums.CreditStatus;

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
