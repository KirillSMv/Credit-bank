package ru.development.Deal.client;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.development.Deal.error_handler.LoanRefusalException;
import ru.development.Deal.model.dto.*;
import ru.development.Deal.model.enums.EmploymentStatus;
import ru.development.Deal.model.enums.Gender;
import ru.development.Deal.model.enums.MaritalStatus;
import ru.development.Deal.model.enums.PositionType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.client.WebClient.*;

@ExtendWith(MockitoExtension.class)
class DealWebClientImplTest {

    @Mock
    private WebClient webClient;
    @Mock
    private CalculatorMSProperties calculatorMSProperties;
    @Mock
    private RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private RequestBodySpec requestBodySpec;
    @Mock
    private RequestHeadersSpec requestHeadersSpec;
    @Mock
    private ResponseSpec responseSpec;
    @InjectMocks
    private DealWebClientImpl dealWebClientMock;

    private static ScoringDataDto scoringDataDto;
    private static CreditDto creditDto;

    @BeforeAll
    static void setup() {
        scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(12)
                .firstName("Ilya")
                .lastName("Shatkov")
                .middleName("Levin")
                .gender(Gender.MALE)
                .employment(EmploymentDto.builder()
                        .employerINN("1111111111")
                        .employmentStatus(EmploymentStatus.UNEMPLOYED)
                        .salary(BigDecimal.valueOf(90000))
                        .positionType(PositionType.TOP_MANAGER)
                        .workExperienceTotal(120)
                        .workExperienceCurrent(120)
                        .build())
                .birthdate(LocalDate.of(1995, 12, 7))
                .passportSeries("4040")
                .passportNumber("111111")
                .passportIssueDate(LocalDate.of(2010, 07, 12))
                .passportIssueBranch("MVD2020")
                .dependentAmount(0)
                .maritalStatus(MaritalStatus.MARRIED)
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        creditDto = CreditDto.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(9504.42))
                .rate(BigDecimal.valueOf(25))
                .psk(BigDecimal.valueOf(14.05))
                .paymentSchedule(getPaymentSchedule())
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();
    }

    @Test
    void calculateOffersTest() {
        LoanStatementRequestDto dto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(12)
                .firstName("Ilya")
                .lastName("Shatkov")
                .middleName("Levin")
                .birthdate(LocalDate.of(1995, 12, 7))
                .email("Ilya@yandex.ru")
                .passportSeries("4040")
                .passportNumber("111111")
                .build();

        List<LoanOfferDto> expectedList = new ArrayList<>();
        expectedList.add(new LoanOfferDto(UUID.fromString("c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22"), BigDecimal.valueOf(500000),
                BigDecimal.valueOf(576104.40).setScale(2, RoundingMode.HALF_UP), 12, BigDecimal.valueOf(48008.70).setScale(2, RoundingMode.HALF_UP), BigDecimal.valueOf(27),
                false, false));
        expectedList.add(new LoanOfferDto(UUID.fromString("c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22"), BigDecimal.valueOf(500000),
                BigDecimal.valueOf(573180.84), 12, BigDecimal.valueOf(47765.07), BigDecimal.valueOf(26),
                false, true));
        expectedList.add(new LoanOfferDto(UUID.fromString("c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22"), BigDecimal.valueOf(500000),
                BigDecimal.valueOf(584644.44), 12, BigDecimal.valueOf(48720.37), BigDecimal.valueOf(26),
                true, false));
        expectedList.add(new LoanOfferDto(UUID.fromString("c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22"), BigDecimal.valueOf(500000),
                BigDecimal.valueOf(581670.48), 12, BigDecimal.valueOf(48472.54), BigDecimal.valueOf(25),
                true, true));
        expectedList = expectedList.stream().sorted(Comparator.comparing(LoanOfferDto::getRate).reversed()).toList();

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/calculator/offers")).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(new ParameterizedTypeReference<List<LoanOfferDto>>() {
        })).thenReturn(Mono.just(expectedList));
        when(calculatorMSProperties.getCalculateOffersUri()).thenReturn("/calculator/offers");


        List<LoanOfferDto> resultList = dealWebClientMock.calculateOffers(dto);

        assertEquals(expectedList, resultList);
    }

    @Test
    void calculateOffersTest_whenCalculatorMSThrownForbiddenException() {
        LoanStatementRequestDto dto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(12)
                .firstName("Ilya")
                .lastName("Shatkov")
                .middleName("Levin")
                .birthdate(LocalDate.of(1995, 12, 7))
                .email("Ilya@yandex.ru")
                .passportSeries("4040")
                .passportNumber("111111")
                .build();

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/calculator/offers")).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenThrow(LoanRefusalException.class);
        when(calculatorMSProperties.getCalculateOffersUri()).thenReturn("/calculator/offers");

        assertThrows(LoanRefusalException.class, () -> dealWebClientMock.calculateOffers(dto));
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void calculateCreditParametersTest() {
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/calculator/calc")).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CreditDto.class)).thenReturn(Mono.just(creditDto));
        when(calculatorMSProperties.getCalculateCreditParametersUri()).thenReturn("/calculator/calc");

        CreditDto resultCreditDto = dealWebClientMock.calculateCreditParameters(scoringDataDto);

        assertEquals(creditDto, resultCreditDto);
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void calculateCreditParametersTest_whenCalculatorMSThrownForbiddenException() {
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/calculator/calc")).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenThrow(LoanRefusalException.class);
        when(responseSpec.bodyToMono(CreditDto.class)).thenReturn(Mono.just(creditDto));
        when(calculatorMSProperties.getCalculateCreditParametersUri()).thenReturn("/calculator/calc");

        assertThrows(LoanRefusalException.class, () -> dealWebClientMock.calculateCreditParameters(scoringDataDto));
    }

    private static List<PaymentScheduleElementDto> getPaymentSchedule() {
        List<PaymentScheduleElementDto> list = new ArrayList<>();
        list.add(PaymentScheduleElementDto.builder()
                .number(1)
                .date(LocalDate.of(2025, 1, 11))
                .totalPayment(BigDecimal.valueOf(9504.42))
                .interestPayment(BigDecimal.valueOf(2083.33))
                .debtPayment(BigDecimal.valueOf(7421.09))
                .remainingDebt(BigDecimal.valueOf(92578.91))
                .build());
        list.add(PaymentScheduleElementDto.builder()
                .number(2)
                .date(LocalDate.of(2025, 2, 11))
                .totalPayment(BigDecimal.valueOf(9504.42))
                .interestPayment(BigDecimal.valueOf(1928.73))
                .debtPayment(BigDecimal.valueOf(7575.69))
                .remainingDebt(BigDecimal.valueOf(85003.22))
                .build());

        list.add(PaymentScheduleElementDto.builder()
                .number(3)
                .date(LocalDate.of(2025, 3, 11))
                .totalPayment(BigDecimal.valueOf(9504.42))
                .interestPayment(BigDecimal.valueOf(1770.90))
                .debtPayment(BigDecimal.valueOf(7733.52))
                .remainingDebt(BigDecimal.valueOf(77269.70))
                .build());

        list.add(PaymentScheduleElementDto.builder()
                .number(4)
                .date(LocalDate.of(2025, 4, 11))
                .totalPayment(BigDecimal.valueOf(9504.42))
                .interestPayment(BigDecimal.valueOf(1609.79))
                .debtPayment(BigDecimal.valueOf(7894.63))
                .remainingDebt(BigDecimal.valueOf(69375.07))
                .build());

        list.add(PaymentScheduleElementDto.builder()
                .number(5)
                .date(LocalDate.of(2025, 5, 11))
                .totalPayment(BigDecimal.valueOf(9504.42))
                .interestPayment(BigDecimal.valueOf(1445.31))
                .debtPayment(BigDecimal.valueOf(8059.11))
                .remainingDebt(BigDecimal.valueOf(61315.96))
                .build());

        list.add(PaymentScheduleElementDto.builder()
                .number(6)
                .date(LocalDate.of(2025, 6, 11))
                .totalPayment(BigDecimal.valueOf(9504.42))
                .interestPayment(BigDecimal.valueOf(1277.42))
                .debtPayment(BigDecimal.valueOf(8227.00))
                .remainingDebt(BigDecimal.valueOf(53088.95))
                .build());

        list.add(PaymentScheduleElementDto.builder()
                .number(7)
                .date(LocalDate.of(2025, 7, 11))
                .totalPayment(BigDecimal.valueOf(9504.42))
                .interestPayment(BigDecimal.valueOf(1106.02))
                .debtPayment(BigDecimal.valueOf(8398.40))
                .remainingDebt(BigDecimal.valueOf(44690.55))
                .build());

        list.add(PaymentScheduleElementDto.builder()
                .number(8)
                .date(LocalDate.of(2025, 8, 11))
                .totalPayment(BigDecimal.valueOf(9504.42))
                .interestPayment(BigDecimal.valueOf(931.05))
                .debtPayment(BigDecimal.valueOf(8573.37))
                .remainingDebt(BigDecimal.valueOf(36117.19))
                .build());

        list.add(PaymentScheduleElementDto.builder()
                .number(9)
                .date(LocalDate.of(2025, 9, 11))
                .totalPayment(BigDecimal.valueOf(9504.42))
                .interestPayment(BigDecimal.valueOf(752.44))
                .debtPayment(BigDecimal.valueOf(8751.98))
                .remainingDebt(BigDecimal.valueOf(27365.21))
                .build());


        list.add(PaymentScheduleElementDto.builder()
                .number(10)
                .date(LocalDate.of(2025, 10, 11))
                .totalPayment(BigDecimal.valueOf(9504.42))
                .interestPayment(BigDecimal.valueOf(570.11))
                .debtPayment(BigDecimal.valueOf(8934.31))
                .remainingDebt(BigDecimal.valueOf(18430.90))
                .build());

        list.add(PaymentScheduleElementDto.builder()
                .number(11)
                .date(LocalDate.of(2025, 11, 11))
                .totalPayment(BigDecimal.valueOf(9504.42))
                .interestPayment(BigDecimal.valueOf(383.98))
                .debtPayment(BigDecimal.valueOf(9120.44))
                .remainingDebt(BigDecimal.valueOf(9310.45))
                .build());

        list.add(PaymentScheduleElementDto.builder()
                .number(12)
                .date(LocalDate.of(2025, 12, 11))
                .totalPayment(BigDecimal.valueOf(9504.42))
                .interestPayment(BigDecimal.valueOf(193.97))
                .debtPayment(BigDecimal.valueOf(9310.45))
                .remainingDebt(BigDecimal.valueOf(0.00))
                .build());
        return list;
    }
}