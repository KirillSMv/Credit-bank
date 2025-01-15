package ru.development.calculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.development.calculator.model.dto.PaymentScheduleElementDto;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class CreditParameters {
    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private BigDecimal psk;
    private List<PaymentScheduleElementDto> paymentSchedule;
}
