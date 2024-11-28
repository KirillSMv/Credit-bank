package ru.development.calculator.service.credit_conditions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.development.calculator.model.dto.LoanOfferDto;
import ru.development.calculator.model.dto.LoanStatementRequestFullDto;
import ru.development.calculator.service.prescoring.PreScoringService;
import ru.development.calculator.service.properties.CreditProperties;
import ru.development.calculator.service.support.interfaces.AnnuityLoanMonthlyPaymentCalculator;
import ru.development.calculator.service.support.interfaces.InsuranceManager;
import ru.development.calculator.service.support.interfaces.SalaryClientsManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditConditionsServiceImpl implements CreditConditionsService {
    private final CreditProperties creditProperties;
    private final PreScoringService preScoringService;
    private final InsuranceManager insuranceManager;
    private final SalaryClientsManager salaryClientsManager;
    private final AnnuityLoanMonthlyPaymentCalculator annuityLoanMonthlyPaymentCalculator;

    @Override
    public List<LoanOfferDto> calculateCreditConditions(LoanStatementRequestFullDto loanStatementRequestFullDto) {
        preScoringService.preScoring(loanStatementRequestFullDto);
        List<LoanOfferDto> offers = new ArrayList<>();
        offers.add(salaryClientsWithoutInsurance(loanStatementRequestFullDto));
        offers.add(salaryClientsWithInsurance(loanStatementRequestFullDto));
        offers.add(usualClientsWithInsurance(loanStatementRequestFullDto));
        offers.add(usualClientsWithoutInsurance(loanStatementRequestFullDto));
        return offers.stream().sorted(Comparator.comparing(LoanOfferDto::getRate).reversed()).toList();
    }

    private LoanOfferDto salaryClientsWithoutInsurance(LoanStatementRequestFullDto dto) {
        BigDecimal normalRate = getNormalRateForLoan();
        BigDecimal salaryClientRate = salaryClientsManager.getRateForSalaryClient(normalRate);
        BigDecimal requestedAmount = dto.getAmount();
        int term = dto.getTerm();

        log.debug("test {}, {},{},", salaryClientRate, requestedAmount, term);
        BigDecimal monthlyPayment = annuityLoanMonthlyPaymentCalculator.getMonthlyPayment(salaryClientRate, requestedAmount, term)
                .setScale(creditProperties.getFinalCalcAccuracy(), RoundingMode.HALF_UP);
        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(term)).setScale(creditProperties.getFinalCalcAccuracy(), RoundingMode.HALF_UP);

        log.debug("метод salaryClientsWithoutInsurance(), rate = {}, totalAmount: {}", salaryClientRate, totalAmount);
        return new LoanOfferDto(dto.getStatementId(), requestedAmount, totalAmount, term, monthlyPayment, salaryClientRate, false, true);
    }

    private LoanOfferDto salaryClientsWithInsurance(LoanStatementRequestFullDto dto) {
        BigDecimal normalRate = getNormalRateForLoan();
        BigDecimal salaryClientRate = salaryClientsManager.getRateForSalaryClient(normalRate);
        BigDecimal insuranceRate = insuranceManager.getRateWithInsurance(salaryClientRate);

        BigDecimal requestedAmount = dto.getAmount();
        BigDecimal amountWithInsurance = insuranceManager.getAmountWithInsurance(requestedAmount);
        int term = dto.getTerm();

        log.debug("test {},{},{},", insuranceRate, amountWithInsurance, term);
        BigDecimal monthlyPayment = annuityLoanMonthlyPaymentCalculator.getMonthlyPayment(insuranceRate, amountWithInsurance, term)
                .setScale(creditProperties.getFinalCalcAccuracy(), RoundingMode.HALF_UP);

        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(term)).setScale(creditProperties.getFinalCalcAccuracy(), RoundingMode.HALF_UP);
        log.debug("метод salaryClientsWithInsurance(), rate = {}, totalAmount: {}", insuranceRate, totalAmount);
        return new LoanOfferDto(dto.getStatementId(), requestedAmount, totalAmount, term, monthlyPayment, insuranceRate,
                true, true);
    }

    private LoanOfferDto usualClientsWithInsurance(LoanStatementRequestFullDto dto) {
        BigDecimal rate = getNormalRateForLoan();
        BigDecimal insuranceRate = insuranceManager.getRateWithInsurance(rate);
        BigDecimal requestedAmount = dto.getAmount();
        BigDecimal amountWithInsurance = insuranceManager.getAmountWithInsurance(requestedAmount);
        int term = dto.getTerm();

        BigDecimal monthlyPayment = annuityLoanMonthlyPaymentCalculator.getMonthlyPayment(insuranceRate, amountWithInsurance, term)
                .setScale(creditProperties.getFinalCalcAccuracy(), RoundingMode.HALF_UP);
        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(term)).setScale(creditProperties.getFinalCalcAccuracy(), RoundingMode.HALF_UP);
        log.debug("метод normalClientsWithInsurance(), rate = {}, totalAmount: {}", insuranceRate, totalAmount);
        return new LoanOfferDto(dto.getStatementId(), requestedAmount, totalAmount, term, monthlyPayment, insuranceRate,
                true, false);

    }

    private LoanOfferDto usualClientsWithoutInsurance(LoanStatementRequestFullDto dto) {
        BigDecimal rate = getNormalRateForLoan();
        BigDecimal requestedAmount = dto.getAmount();
        int term = dto.getTerm();

        BigDecimal monthlyPayment = annuityLoanMonthlyPaymentCalculator.getMonthlyPayment(rate, requestedAmount, term)
                .setScale(creditProperties.getFinalCalcAccuracy(), RoundingMode.HALF_UP);
        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(term)).setScale(creditProperties.getFinalCalcAccuracy(), RoundingMode.HALF_UP);
        log.debug("метод normalClientsWithoutInsurance(), rate = {}, totalAmount: {}", rate, totalAmount);
        return new LoanOfferDto(dto.getStatementId(), requestedAmount, totalAmount, term, monthlyPayment, rate,
                false, false);
    }

    private BigDecimal getNormalRateForLoan() {
        return creditProperties.getCentralBankInterestRate().add(creditProperties.getAverageRateAboveCentralBInterestRate());
    }
}
