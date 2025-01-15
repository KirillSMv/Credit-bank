package ru.development.statement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.development.statement.model.dto.LoanOfferDto;
import ru.development.statement.model.dto.LoanStatementRequestDto;
import ru.development.statement.service.StatementServiceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatementController.class)
class StatementControllerTest {
    @MockBean
    private StatementServiceImpl serviceMock;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private StatementController statementController;
    private static LoanStatementRequestDto dtoWithInvalidTerm;
    private static LoanStatementRequestDto validLoanStatementDto;
    private static LoanOfferDto offerDto;

    @BeforeAll
    static void setUp() {
        dtoWithInvalidTerm = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(500000))
                .term(0)
                .firstName("Ilya")
                .lastName("Shatkov")
                .middleName("Levin")
                .birthdate(LocalDate.of(1995, 12, 7))
                .email("Ilya@yandex.ru")
                .passportSeries("4040")
                .passportNumber("111111")
                .build();

        validLoanStatementDto = LoanStatementRequestDto.builder()
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
    void processLoanStatementTest_whenInvalidDtoPassed_thenThrowMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoWithInvalidTerm)))
                .andExpect(status().is(400))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

    }

    @Test
    void processLoanStatementTest_whenValidDtoPassed_thenReturnListOfLoanOffers() throws Exception {
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

        when(serviceMock.processLoanStatement(any(LoanStatementRequestDto.class))).thenReturn(expectedList);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoanStatementDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].statementIdUuid", is("c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22")))
                .andExpect(jsonPath("$[0].requestedAmount", is(500000)))
                .andExpect(jsonPath("$[0].totalAmount", is(576104.40)))
                .andExpect(jsonPath("$[0].term", is(12)))
                .andExpect(jsonPath("$[0].monthlyPayment", is(48008.70)))
                .andExpect(jsonPath("$[0].rate", is(27)))
                .andExpect(jsonPath("$[0].isInsuranceEnabled", is(false)))
                .andExpect(jsonPath("$[0].isSalaryClient", is(false)));
    }

    @Test
    void selectOfferTest() throws Exception {
        doNothing().when(serviceMock).selectOffer(any(LoanOfferDto.class));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/statement/offer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offerDto)));

        verify(serviceMock, times(1)).selectOffer(offerDto);
    }
}