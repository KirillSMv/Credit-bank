package ru.development.gateway.deal_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.development.gateway.deal_service.client.DealClient;
import ru.development.gateway.model.Client;
import ru.development.gateway.model.Passport;
import ru.development.gateway.model.Statement;
import ru.development.gateway.model.StatusHistory;
import ru.development.gateway.model.dto.EmploymentDto;
import ru.development.gateway.model.dto.FinishRegistrationRequestDto;
import ru.development.gateway.model.dto.LoanStatementRequestDto;
import ru.development.gateway.model.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DealController.class)
class DealControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DealClient dealClient;
    @InjectMocks
    private DealController dealController;
    private static FinishRegistrationRequestDto finishRegistrationRequestDto;
    private static FinishRegistrationRequestDto invalidFinishRegistrationRequestDto;
    private static String statementId = "63e8f05d-6d53-49b0-a4f5-80939b31a0d8";
    private static Client client;
    private static Statement statement;
    private static LoanStatementRequestDto loanStatementRequestDto;

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

        invalidFinishRegistrationRequestDto = FinishRegistrationRequestDto.builder()
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(20)
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
                .statementIdUuid(UUID.fromString(statementId))
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
    void finalizeLoanParametersTest() throws Exception {
        doNothing().when(dealClient).finalizeLoanParameters(finishRegistrationRequestDto, statementId);
        mockMvc.perform(MockMvcRequestBuilders.post("/statement/registration/{statementId}", statementId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(finishRegistrationRequestDto)))
                .andExpect(status().isOk());

        Mockito.verify(dealClient).finalizeLoanParameters(finishRegistrationRequestDto, statementId);
    }

    @Test
    void finalizeLoanParametersTest_whenInvalidRequestBody_thenThrowMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/statement/registration/{statementId}", statementId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidFinishRegistrationRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void sendDocumentsTest() throws Exception {
        doNothing().when(dealClient).sendDocuments(statementId);
        mockMvc.perform(MockMvcRequestBuilders.post("/document/{statementId}", statementId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(dealClient).sendDocuments(statementId);
    }

    @Test
    void signDocumentsTest() throws Exception {
        doNothing().when(dealClient).signDocuments(statementId);
        mockMvc.perform(MockMvcRequestBuilders.post("/document/{statementId}/sign", statementId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(dealClient).signDocuments(statementId);
    }

    @Test
    void processSesCodeTest() throws Exception {
        String code = "4324";
        doNothing().when(dealClient).processSesCode(statementId, code);
        mockMvc.perform(MockMvcRequestBuilders.post("/document/{statementId}/sign/code", statementId)
                        .param("code", code)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(dealClient).processSesCode(statementId, code);
    }

    @Test
    void getStatementByIdTest() throws Exception {
        when(dealClient.getStatementById(statementId)).thenReturn(statement);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/statement/{statementId}", statementId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("statementIdUuid", Matchers.is(statementId)));
    }

    @Test
    void getStatementsTest() throws Exception {
        Integer offset = 0;
        Integer size = 1;
        when(dealClient.getStatements(offset, size)).thenReturn(List.of(statement));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/statement", statementId)
                        .param("offset", String.valueOf(offset))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]statementIdUuid", Matchers.is(statementId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]client.firstName", Matchers.is(client.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]client.lastName", Matchers.is(client.getLastName())));
    }
}