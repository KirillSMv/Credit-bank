package ru.development.calculator.service.prescoring;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.development.calculator.error_handler.PrescoringException;
import ru.development.calculator.model.dto.LoanStatementRequestFullDto;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PreScoringServiceImplTest {
    private static LoanStatementRequestFullDto loanStatementRequestFullDto;
    private static LoanStatementRequestFullDto requestDtoWithLowAge;
    private static LoanStatementRequestFullDto requestDtoWithHighAge;
    private static PreScoringService preScoringService;

    @BeforeAll
    static void get() {
        preScoringService = new PreScoringServiceImpl(20, 65);

        loanStatementRequestFullDto = LoanStatementRequestFullDto.builder()
                .statementId(UUID.randomUUID())
                .amount(BigDecimal.valueOf(500000))
                .term(12)
                .firstName("Ilya")
                .lastName("Shatkov")
                .middleName("Levin")
                .birthdate(LocalDate.of(1995, 12, 7))
                .email("Ilya@yandex.ru")
                .passportSeries("4040")
                .passportNumber("111111")
                .build();

        requestDtoWithLowAge = LoanStatementRequestFullDto.builder()
                .statementId(UUID.randomUUID())
                .amount(BigDecimal.valueOf(500000))
                .term(12)
                .firstName("Ilya")
                .lastName("Shatkov")
                .middleName("Levin")
                .birthdate(LocalDate.of(2010, 12, 7))
                .email("Ilya@yandex.ru")
                .passportSeries("4040")
                .passportNumber("111111")
                .build();

        requestDtoWithHighAge = LoanStatementRequestFullDto.builder()
                .statementId(UUID.randomUUID())
                .amount(BigDecimal.valueOf(500000))
                .term(12)
                .firstName("Ilya")
                .lastName("Shatkov")
                .middleName("Levin")
                .birthdate(LocalDate.of(1950, 12, 7))
                .email("Ilya@yandex.ru")
                .passportSeries("4040")
                .passportNumber("111111")
                .build();
    }

    @Test
    void preScoringTest_whenCriteriaMet_thenPassed() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        //тест конретно для текущей реализации (заглушки) методов валидации
        String passportValidationPrint = "Паспорт является действительным, гражданин не находится в розыске, " +
                "в списке террористов и экстремистов, долгов по ФНС не числится";
        String creditHistoryValidationPrint = "Отрицательной кредитной истории не выявлено";

        preScoringService.preScoring(loanStatementRequestFullDto);

        String expectedOutput = passportValidationPrint + System.lineSeparator() + creditHistoryValidationPrint
                + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }


    @Test
    void preScoringTest_whenAgeIsBelowLimit_thenThrowPrescoringException() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        assertThrows(PrescoringException.class, () ->
                preScoringService.preScoring(requestDtoWithLowAge));
    }

    @Test
    void preScoringTest_whenAgeIsAboveLimit_thenThrowPrescoringException() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        assertThrows(PrescoringException.class, () ->
                preScoringService.preScoring(requestDtoWithHighAge));
    }
}