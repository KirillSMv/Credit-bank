package ru.development.calculator.service.prescoring;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.development.calculator.error_handler.PrescoringException;
import ru.development.calculator.model.dto.LoanStatementRequestDto;

import java.time.LocalDate;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class PreScoringServiceImpl implements PreScoringService {
    @Value("${clientMinYear}")
    private Integer clientMinYear;
    @Value("${clientMaxYear}")
    private Integer clientMaxYear;

    @Override
    public void preScoring(LoanStatementRequestDto loanStatementRequestDto) {
        log.debug("preScoring заявки");
        ageValidation(loanStatementRequestDto.getBirthdate());
    }

    private void ageValidation(LocalDate birthdate) {
        boolean isYounger20Years = birthdate.isAfter(LocalDate.now().minusYears(clientMinYear));
        boolean isOlder65Years = birthdate.isBefore(LocalDate.now().minusYears(clientMaxYear));
        if (isYounger20Years || isOlder65Years) {
            log.warn("В кредите отказано по причине несоответствия возраста, birthdate = {}, clientMinYear = {}, " +
                    "clientMaxYear = {}", birthdate, clientMinYear, clientMaxYear);
            throw new PrescoringException("К сожалению, вам отказано в кредите");
        }
    }
}
