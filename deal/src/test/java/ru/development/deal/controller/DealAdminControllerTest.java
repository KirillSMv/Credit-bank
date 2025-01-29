package ru.development.deal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.development.deal.model.Client;
import ru.development.deal.model.Passport;
import ru.development.deal.model.Statement;
import ru.development.deal.model.StatusHistory;
import ru.development.deal.model.dto.LoanStatementRequestDto;
import ru.development.deal.model.enums.ApplicationStatus;
import ru.development.deal.model.enums.ChangeType;
import ru.development.deal.service.interfaces.DealAdminService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DealAdminController.class)
class DealAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private DealAdminController dealAdminController;

    @MockBean
    private DealAdminService dealAdminService;

    @Autowired
    private ObjectMapper objectMapper;
    private static Statement statement;
    private static LoanStatementRequestDto loanStatementRequestDto;

    @BeforeAll
    public static void setUp() {
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

        statement = Statement.builder()
                .statementIdUuid(UUID.fromString("c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22"))
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
    void updateStatementDocStatusTest() throws Exception {
        doNothing().when(dealAdminService).updateStatementDocStatus(any());

        mockMvc.perform(MockMvcRequestBuilders.put("/deal/admin/statement/{statementId}/status", "c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(dealAdminService).updateStatementDocStatus(any());
    }

    @Test
    void getStatementByIdTest() throws Exception {
        when(dealAdminService.getStatementById(any(String.class))).thenReturn(statement);

        mockMvc.perform(MockMvcRequestBuilders.get("/deal/admin/statement/{statementId}", "c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statementIdUuid", Matchers.is("c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22")));

        verify(dealAdminService).getStatementById(any(String.class));
    }

    @Test
    void testGetStatementByIdTest() throws Exception {
        when(dealAdminService.getStatements(any(PageRequest.class))).thenReturn(List.of(statement));

        mockMvc.perform(MockMvcRequestBuilders.get("/deal/admin/statement")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].statementIdUuid", Matchers.is("c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22")));

        verify(dealAdminService).getStatements(any(PageRequest.class));

    }
}