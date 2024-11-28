package ru.development.calculator.service.prescoring;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.development.calculator.error_handler.PrescoringException;
import ru.development.calculator.model.dto.LoanStatementRequestFullDto;

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
    public void preScoring(LoanStatementRequestFullDto loanStatementRequestFullDto) {
        log.debug("preScoring заявки statementId: {}", loanStatementRequestFullDto.getStatementId());
        ageValidation(loanStatementRequestFullDto.getBirthdate());
        passportValidation(loanStatementRequestFullDto.getPassportSeries(), loanStatementRequestFullDto.getPassportNumber());
        creditHistoryValidation(loanStatementRequestFullDto.getFirstName(), loanStatementRequestFullDto.getLastName(),
                loanStatementRequestFullDto.getMiddleName());
    }

    private void ageValidation(LocalDate birthdate) {
        boolean isYounger20Years = birthdate.isAfter(LocalDate.now().minusYears(clientMinYear));
        boolean isOlder65Years = birthdate.isBefore(LocalDate.now().minusYears(clientMaxYear));
        if (isYounger20Years || isOlder65Years) {
            log.warn("В кредите отказано по причине несоответствния возраста, birthdate = {}, clientMinYear = {}, " +
                    "clientMaxYear = {}", birthdate, clientMinYear, clientMaxYear);
            throw new PrescoringException("К сожалению, вам отказано в кредите");
        }
    }

    private void passportValidation(String passportSeries, String passportNumber) {
        //имитируем проверку клиента по базам данных федеральных служб
        System.out.println("Паспорт является действительным, гражданин не находится в розыске, " +
                "в списке террористов и экстремистов, долгов по ФНС не числится");
    }

    private void creditHistoryValidation(String firstName, String lastName, String middleName) {
        //имитируем проверку кредитной истории клиента
        System.out.println("Отрицательной кредитной истории не выявлено");
    }
}
