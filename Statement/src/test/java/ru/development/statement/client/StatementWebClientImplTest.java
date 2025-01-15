package ru.development.statement.client;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.development.statement.exceptions.ErrorProcessingRequest;
import ru.development.statement.exceptions.LoanRefusalException;
import ru.development.statement.model.dto.LoanOfferDto;
import ru.development.statement.model.dto.LoanStatementRequestDto;

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
import static org.mockito.Mockito.*;
import static org.springframework.web.reactive.function.client.WebClient.*;

@ExtendWith(MockitoExtension.class)
class StatementWebClientImplTest {

    @Mock
    private DealMSProperties dealMSProperties;
    @Mock
    private WebClient webClientMock;
    @InjectMocks
    private StatementWebClientImpl statementWebClient;
    private static LoanStatementRequestDto loanStatementDto;
    private static LoanOfferDto offerDto;
    @Mock
    private RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private RequestBodySpec requestBodySpec;

    @Mock
    private RequestHeadersSpec requestHeadersSpec;
    @Mock
    private ResponseSpec responseSpec;

    @BeforeAll
    static void setUp() {

        loanStatementDto = LoanStatementRequestDto.builder()
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

        offerDto = LoanOfferDto.builder()
                .statementIdUuid(UUID.fromString("c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22"))
                .requestedAmount(BigDecimal.valueOf(500000))
                .totalAmount(BigDecimal.valueOf(576104.40))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(48008.70))
                .rate(BigDecimal.valueOf(27))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();
    }

    @Test
    void requestLoanOffersTest() {
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

        when(dealMSProperties.getProcessLoanStatementUrl()).thenReturn("/deal/statement");
        Mockito.when(webClientMock.post()).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.uri("/deal/statement")).thenReturn(requestBodySpec);
        Mockito.when(requestBodySpec.bodyValue(loanStatementDto)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(new ParameterizedTypeReference<List<LoanOfferDto>>() {
        })).thenReturn(Mono.just(expectedList));

        List<LoanOfferDto> resultList = statementWebClient.requestLoanOffers(loanStatementDto);
        assertEquals(expectedList, resultList);
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void requestLoanOffersTest_whenServerResponseStatusIs400_thenThrowErrorProcessingRequestException() {
        when(dealMSProperties.getProcessLoanStatementUrl()).thenReturn("/deal/statement");
        Mockito.when(webClientMock.post()).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.uri("/deal/statement")).thenReturn(requestBodySpec);
        Mockito.when(requestBodySpec.bodyValue(loanStatementDto)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenThrow(ErrorProcessingRequest.class);

        assertThrows(ErrorProcessingRequest.class, () -> statementWebClient.requestLoanOffers(loanStatementDto));
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void requestLoanOffersTest_whenServerResponseStatusIs409_thenThrowLoanRefusalException() {
        when(dealMSProperties.getProcessLoanStatementUrl()).thenReturn("/deal/statement");
        Mockito.when(webClientMock.post()).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.uri("/deal/statement")).thenReturn(requestBodySpec);
        Mockito.when(requestBodySpec.bodyValue(loanStatementDto)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenThrow(LoanRefusalException.class);

        assertThrows(LoanRefusalException.class, () -> statementWebClient.requestLoanOffers(loanStatementDto));
    }


    @Test
    void selectOfferTest() {
        when(dealMSProperties.getSelectLoanOfferUrl()).thenReturn("/deal/offer/select");
        Mockito.when(webClientMock.post()).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.uri("/deal/offer/select")).thenReturn(requestBodySpec);
        Mockito.when(requestBodySpec.bodyValue(offerDto)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        statementWebClient.selectOffer(offerDto);

        verify(dealMSProperties, times(1)).getSelectLoanOfferUrl();
    }

    @Test
    void selectOfferTest_whenServerResponseStatusIs400_thenThrowErrorProcessingRequestException() {
        when(dealMSProperties.getSelectLoanOfferUrl()).thenReturn("/deal/offer/select");
        Mockito.when(webClientMock.post()).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.uri("/deal/offer/select")).thenReturn(requestBodySpec);
        Mockito.when(requestBodySpec.bodyValue(offerDto)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenThrow(ErrorProcessingRequest.class);

        assertThrows(ErrorProcessingRequest.class, () -> statementWebClient.selectOffer(offerDto));
    }

    @Test
    void selectOfferTest_whenServerResponseStatusIs409_thenThrowLoanRefusalException() {
        when(dealMSProperties.getSelectLoanOfferUrl()).thenReturn("/deal/offer/select");
        Mockito.when(webClientMock.post()).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.uri("/deal/offer/select")).thenReturn(requestBodySpec);
        Mockito.when(requestBodySpec.bodyValue(offerDto)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenThrow(LoanRefusalException.class);

        assertThrows(LoanRefusalException.class, () -> statementWebClient.selectOffer(offerDto));
    }
}