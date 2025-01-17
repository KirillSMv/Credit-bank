package ru.development.gateway.deal_service.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import ru.development.gateway.model.Client;
import ru.development.gateway.model.Passport;
import ru.development.gateway.model.Statement;
import ru.development.gateway.model.StatusHistory;
import ru.development.gateway.model.dto.EmploymentDto;
import ru.development.gateway.model.dto.FinishRegistrationRequestDto;
import ru.development.gateway.model.dto.LoanStatementRequestDto;
import ru.development.gateway.model.enums.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.mockito.Mockito.*;
import static org.springframework.web.reactive.function.client.WebClient.*;

@ExtendWith(MockitoExtension.class)
class DealWebClientImplTest {
    @Mock
    private WebClient webClient;
    @Mock
    private DealMSProperties dealMSProperties;
    @Mock
    private RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private RequestBodySpec requestBodySpec;
    @Mock
    private RequestHeadersSpec requestHeadersSpec;
    @Mock
    private ResponseSpec responseSpec;

    @Mock
    private RequestHeadersUriSpec requestHeadersUriSpec;
    @InjectMocks
    private DealWebClientImpl dealWebClient;
    private String statementId = "63e8f05d-6d53-49b0-a4f5-80939b31a0d8";
    private String dealServerUrl = "http://localhost:9090";
    private static FinishRegistrationRequestDto finishRegistrationRequestDto;
    private static LoanStatementRequestDto loanStatementRequestDto;
    private static Client client;
    private static Statement statement;


    @BeforeAll
    static void setUp() {
        finishRegistrationRequestDto = FinishRegistrationRequestDto.builder()
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(0)
                .passportIssueDate(LocalDate.of(2010, 07, 12))
                .passportIssueBranch("MVD2020")
                .employmentDto(EmploymentDto.builder()
                        .employerINN("1111111111")
                        .employmentStatus(EmploymentStatus.UNEMPLOYED)
                        .salary(BigDecimal.valueOf(90000))
                        .positionType(PositionType.TOP_MANAGER)
                        .workExperienceTotal(120)
                        .workExperienceCurrent(120)
                        .build())
                .build();

        loanStatementRequestDto = LoanStatementRequestDto.builder()
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

        client = Client.builder()
                .firstName("Ilya")
                .lastName("Shatkov")
                .middleName("Levin")
                .birthdate(LocalDate.of(1995, 12, 7))
                .email("Ilya@yandex.ru")
                .passport(Passport.builder().series("4040").number("111111").build())
                .build();

        statement = Statement.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .creationDate(LocalDateTime.now())
                .client(Client.builder()
                        .lastName(loanStatementRequestDto.getLastName())
                        .firstName(loanStatementRequestDto.getFirstName())
                        .middleName(loanStatementRequestDto.getMiddleName())
                        .birthdate(loanStatementRequestDto.getBirthdate())
                        .email(loanStatementRequestDto.getEmail())
                        .passport(Passport.builder()
                                .series(loanStatementRequestDto.getPassportSeries())
                                .number(loanStatementRequestDto.getPassportNumber())
                                .build())
                        .build())
                .statusHistory(new ArrayList<>(List.of(StatusHistory.builder()
                        .status(ApplicationStatus.PREAPPROVAL)
                        .time(LocalDateTime.now())
                        .changeType(ChangeType.AUTOMATIC)
                        .build())))
                .build();
    }


    @Test
    void finalizeLoanParametersTest() {
        String finalizeLoanParametersUrl = "http://localhost:9090/deal/calculate/{statementId}";
        when(dealMSProperties.getFinalizeLoanParametersUrl()).thenReturn(finalizeLoanParametersUrl);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(finalizeLoanParametersUrl, statementId)).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(finishRegistrationRequestDto)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        dealWebClient.finalizeLoanParameters(finishRegistrationRequestDto, statementId);

        verify(dealMSProperties).getFinalizeLoanParametersUrl();
        verify(webClient).post();
        verify(requestBodyUriSpec).uri(finalizeLoanParametersUrl, statementId);
        verify(requestBodySpec).bodyValue(finishRegistrationRequestDto);
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).onStatus(any(), any());
        verify(responseSpec).bodyToMono(Void.class);
    }

    @Test
    void sendDocumentsTest() {
        String sendDocumentsUrl = "http://localhost:9090/deal/document/{statementId}/send";
        when(dealMSProperties.getSendDocumentsUrl()).thenReturn(sendDocumentsUrl);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(sendDocumentsUrl, statementId)).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        dealWebClient.sendDocuments(statementId);

        verify(dealMSProperties).getSendDocumentsUrl();
        verify(webClient).post();
        verify(requestBodyUriSpec).uri(sendDocumentsUrl, statementId);
        verify(requestBodySpec).retrieve();
        verify(responseSpec).onStatus(any(), any());
        verify(responseSpec).bodyToMono(Void.class);
    }

    @Test
    void updateStatementDocStatusTest() {
        String updateStatementDocStatusUrl = "http://localhost:9090/deal/admin/statement/{statementId}/status";
        when(dealMSProperties.getUpdateStatementDocStatusUrl()).thenReturn(updateStatementDocStatusUrl);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(updateStatementDocStatusUrl, statementId)).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        dealWebClient.updateStatementDocStatus(statementId);

        verify(dealMSProperties).getUpdateStatementDocStatusUrl();
        verify(webClient).post();
        verify(requestBodyUriSpec).uri(updateStatementDocStatusUrl, statementId);
        verify(requestBodySpec).retrieve();
        verify(responseSpec).onStatus(any(), any());
        verify(responseSpec).bodyToMono(Void.class);
    }

    @Test
    void processSesCodeTest() {
        String code = "1423";
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(), (Function<UriBuilder, URI>) any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        dealWebClient.processSesCode(statementId, code);

        verify(webClient).post();
        verify(requestBodySpec).retrieve();
        verify(responseSpec).onStatus(any(), any());
        verify(responseSpec).bodyToMono(Void.class);
    }

    @Test
    void signDocuments() {
        String signDocumentsUrl = "http://localhost:9090/deal/document/{statementId}/sign";
        when(dealMSProperties.getSignDocumentsUrl()).thenReturn(signDocumentsUrl);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(signDocumentsUrl, statementId)).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        dealWebClient.signDocuments(statementId);

        verify(dealMSProperties).getSignDocumentsUrl();
        verify(webClient).post();
        verify(requestBodyUriSpec).uri(signDocumentsUrl, statementId);
        verify(requestBodySpec).retrieve();
        verify(responseSpec).onStatus(any(), any());
        verify(responseSpec).bodyToMono(Void.class);
    }

    @Test
    void getStatementsTest() {
        Integer offset = 0;
        Integer size = 1;
        List<Statement> expectedList = new ArrayList<>(List.of(statement));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(), (Function<UriBuilder, URI>) any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(new ParameterizedTypeReference<List<Statement>>() {
        })).thenReturn(Mono.just(expectedList));

        List<Statement> statements = dealWebClient.getStatements(offset, size);

        Assertions.assertEquals(expectedList, statements);
        verify(webClient).get();
        verify(requestBodySpec).retrieve();
        verify(responseSpec).onStatus(any(), any());
    }

    @Test
    void getStatementByIdTest() {
        String statementByIdUrl = "http://localhost:9090/deal/admin/statement/{statementId}";
        when(dealMSProperties.getGetStatementByIdUrl()).thenReturn(statementByIdUrl);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(statementByIdUrl, statementId)).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Statement.class)).thenReturn(Mono.just(statement));

        Statement statementById = dealWebClient.getStatementById(statementId);

        Assertions.assertEquals(statement, statementById);
        verify(dealMSProperties).getGetStatementByIdUrl();
        verify(webClient).get();
        verify(requestHeadersUriSpec).uri(statementByIdUrl, statementId);
        verify(requestBodySpec).retrieve();
        verify(responseSpec).onStatus(any(), any());
        verify(responseSpec).bodyToMono(Statement.class);
    }
}