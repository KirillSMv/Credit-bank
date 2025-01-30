package ru.development.calculator.service.prescoring;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.development.calculator.error_handler.PrescoringException;
import ru.development.calculator.model.dto.LoanStatementRequestDto;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PreScoringServiceImplTest {
    private static LoanStatementRequestDto requestDtoWithLowAge;
    private static LoanStatementRequestDto requestDtoWithHighAge;
    private static PreScoringService preScoringService;

    @BeforeAll
    static void get() {
        preScoringService = new PreScoringServiceImpl(20, 65);

        requestDtoWithLowAge = LoanStatementRequestDto.builder()
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

        requestDtoWithHighAge = LoanStatementRequestDto.builder()
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