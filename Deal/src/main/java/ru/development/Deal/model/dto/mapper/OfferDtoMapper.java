package ru.development.Deal.model.dto.mapper;

import org.springframework.stereotype.Component;
import ru.development.Deal.model.Offer;
import ru.development.Deal.model.dto.LoanOfferDto;

@Component
public class OfferDtoMapper {
    public Offer toOffer(LoanOfferDto dto) {
        return Offer.builder()
                .requestedAmount(dto.getRequestedAmount())
                .totalAmount(dto.getTotalAmount())
                .term(dto.getTerm())
                .monthlyPayment(dto.getMonthlyPayment())
                .rate(dto.getRate())
                .isInsuranceEnabled(dto.getIsInsuranceEnabled())
                .isSalaryClient(dto.getIsSalaryClient())
                .build();
    }
}
