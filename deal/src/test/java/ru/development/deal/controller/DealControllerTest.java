package ru.development.deal.controller;

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
import ru.development.deal.model.dto.EmploymentDto;
import ru.development.deal.model.dto.FinishRegistrationRequestDto;
import ru.development.deal.model.dto.LoanOfferDto;
import ru.development.deal.model.dto.LoanStatementRequestDto;
import ru.development.deal.model.enums.EmploymentStatus;
import ru.development.deal.model.enums.Gender;
import ru.development.deal.model.enums.MaritalStatus;
import ru.development.deal.model.enums.PositionType;
import ru.development.deal.service.interfaces.DealService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DealController.class)
class DealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private DealController dealController;

    @MockBean
    private DealService dealService;

    @Autowired
    private ObjectMapper objectMapper;

    private static FinishRegistrationRequestDto finishRegistrationRequestDto;
    private static LoanStatementRequestDto loanStatementDto;
    private static LoanOfferDto offerDto;

    @BeforeAll
    public static void init() {
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
    }

    @Test
    void processLoanStatementTest() throws Exception {
        when(dealService.processLoanStatement(any(LoanStatementRequestDto.class))).thenReturn(List.of(offerDto));

        mockMvc.perform(MockMvcRequestBuilders.post("/deal/statement")
                        .content(objectMapper.writeValueAsString(loanStatementDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].statementIdUuid", Matchers.is("c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22")));
    }

    @Test
    void selectOfferTest() throws Exception {
        doNothing().when(dealService).selectOffer(any(LoanOfferDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/deal/offer/select")
                        .content(objectMapper.writeValueAsString(offerDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void finalizeLoanParametersTest() throws Exception {
        doNothing().when(dealService).finalizeLoanParameters(any(FinishRegistrationRequestDto.class), any(String.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/deal/calculate/{statementId}", "c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22")
                        .content(objectMapper.writeValueAsString(finishRegistrationRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(dealService).finalizeLoanParameters(finishRegistrationRequestDto, "c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22");
    }

    @Test
    void sendDocumentsTest() throws Exception {
        doNothing().when(dealService).sendDocuments(any(String.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/deal/document/{statementId}/send", "c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22")
                        .content(objectMapper.writeValueAsString(offerDto.getStatementIdUuid()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(dealService).sendDocuments("c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22");
    }

    @Test
    void signDocumentsTest() throws Exception {
        doNothing().when(dealService).signDocuments(any(String.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/deal/document/{statementId}/sign", "c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22")
                        .content(objectMapper.writeValueAsString(offerDto.getStatementIdUuid()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(dealService).signDocuments("c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22");
    }

    @Test
    void processSesCodeTest() throws Exception {
        doNothing().when(dealService).processSesCode(any(String.class), any(String.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/deal/document/{statementId}/code", "c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22")
                        .param("code", "1234")
                        .content(objectMapper.writeValueAsString(offerDto.getStatementIdUuid()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(dealService).processSesCode("c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22", "1234");
    }
}