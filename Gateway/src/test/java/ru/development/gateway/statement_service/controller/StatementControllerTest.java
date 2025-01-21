package ru.development.gateway.statement_service.controller;

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
import ru.development.gateway.model.dto.LoanOfferDto;
import ru.development.gateway.model.dto.LoanStatementRequestDto;
import ru.development.gateway.statement_service.client.StatementClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatementController.class)
class StatementControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StatementClient statementClient;
    @InjectMocks
    private StatementController statementController;
    private static LoanStatementRequestDto loanStatementRequestDto;
    private static LoanOfferDto offerDto;
    private static LoanOfferDto invalidLoanOfferDto;


    @BeforeAll
    static void setUp() {
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

        invalidLoanOfferDto = LoanOfferDto.builder()
                .statementIdUuid(UUID.fromString("c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22"))
                .requestedAmount(BigDecimal.valueOf(500000))
                .totalAmount(BigDecimal.valueOf(576104.40))
                .term(-1)
                .monthlyPayment(BigDecimal.valueOf(48008.70))
                .rate(BigDecimal.valueOf(27))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();
    }

    @Test
    void processLoanStatementTest() throws Exception {
        List<LoanOfferDto> expectedList = List.of(offerDto);
        when(statementClient.processLoanStatement(loanStatementRequestDto)).thenReturn(expectedList);
        mockMvc.perform(MockMvcRequestBuilders.post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanStatementRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].statementIdUuid", Matchers.is("c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22")));
    }

    @Test
    void selectOfferTest() throws Exception {
        doNothing().when(statementClient).selectOffer(offerDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/statement/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offerDto)))
                .andExpect(status().isOk());

        Mockito.verify(statementClient).selectOffer(offerDto);
    }
}