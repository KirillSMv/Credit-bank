package ru.development.calculator.service.credit_parameters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.development.calculator.model.CreditParameters;
import ru.development.calculator.model.CreditParametersShort;
import ru.development.calculator.model.dto.PaymentScheduleElementDto;
import ru.development.calculator.service.properties.CreditProperties;
import ru.development.calculator.service.support.interfaces.AnnuityLoanMonthlyPaymentCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.math.RoundingMode.HALF_UP;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnnuityLoanParametersServiceImpl implements AnnuityLoanParametersService {
    private final AnnuityLoanMonthlyPaymentCalculator annuityLoanMonthlyPaymentCalculator;
    private final CreditProperties creditProperties;

    @Override
    public CreditParameters calculateAnnuityLoanParameters(CreditParametersShort creditParametersShort) {
        BigDecimal monthlyPayment = annuityLoanMonthlyPaymentCalculator.getMonthlyPayment(creditParametersShort.getRate(),
                creditParametersShort.getAmount(), creditParametersShort.getTerm());
        List<PaymentScheduleElementDto> paymentSchedule = getPaymentSchedule(creditParametersShort);
        BigDecimal psk = getLoanPsk(monthlyPayment, creditParametersShort.getAmount(), creditParametersShort.getTerm());

        return new CreditParameters(creditParametersShort.getAmount(), creditParametersShort.getTerm(), monthlyPayment
                .setScale(creditProperties.getFinalCalcAccuracy(), RoundingMode.HALF_UP),
                creditParametersShort.getRate(), psk, paymentSchedule);
    }

    private BigDecimal getLoanPsk(BigDecimal monthlyPayment, BigDecimal amount, Integer term) {
        return ((monthlyPayment.multiply(BigDecimal.valueOf(term))).divide(amount, creditProperties.getInterCalcAccuracy(), HALF_UP))
                .subtract(BigDecimal.valueOf(1))
                .multiply((BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP);
    }

    private List<PaymentScheduleElementDto> getPaymentSchedule(CreditParametersShort creditParametersShort) {
        BigDecimal amount = creditParametersShort.getAmount();
        BigDecimal rate = creditParametersShort.getRate();
        Integer term = creditParametersShort.getTerm();
        BigDecimal monthlyPayment = annuityLoanMonthlyPaymentCalculator.getMonthlyPayment(creditParametersShort.getRate(),
                creditParametersShort.getAmount(), creditParametersShort.getTerm());
        BigDecimal monthlyRate = getMonthlyRate(rate);
        LocalDate loanIssueDate = LocalDate.now().plusDays(1);

        List<PaymentScheduleElementDto> paymentSchedule = new ArrayList<>();
        int number;
        BigDecimal interestPayment;
        BigDecimal debtPayment;
        BigDecimal remainingDebt;
        BigDecimal currentAmountToPay = amount;
        LocalDate paymentDate;

        for (int i = 1; i <= term; i++) {
            paymentDate = loanIssueDate.plusMonths(i);
            number = i;
            interestPayment = currentAmountToPay.multiply(monthlyRate);
            debtPayment = monthlyPayment.subtract(interestPayment);
            remainingDebt = currentAmountToPay.subtract(debtPayment);
            currentAmountToPay = remainingDebt;
            paymentSchedule.add(new PaymentScheduleElementDto(number, paymentDate,
                    monthlyPayment.setScale(creditProperties.getFinalCalcAccuracy(), HALF_UP),
                    interestPayment.setScale(creditProperties.getFinalCalcAccuracy(), HALF_UP),
                    debtPayment.setScale(creditProperties.getFinalCalcAccuracy(), HALF_UP),
                    remainingDebt.setScale(creditProperties.getFinalCalcAccuracy(), HALF_UP)));
        }
        return paymentSchedule;
    }

    private BigDecimal getMonthlyRate(BigDecimal rate) {
        BigDecimal monthlyRateInPercent = rate.divide(BigDecimal.valueOf(12), creditProperties.getInterCalcAccuracy(), HALF_UP);
        return monthlyRateInPercent.divide(BigDecimal.valueOf(100), creditProperties.getInterCalcAccuracy(), HALF_UP);
    }
}
