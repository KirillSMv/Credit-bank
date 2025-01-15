package ru.development.Deal.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.development.Deal.client.interfaces.DealClient;
import ru.development.Deal.error_handler.LoanRefusalException;
import ru.development.Deal.error_handler.NoObjectFoundException;
import ru.development.Deal.model.*;
import ru.development.Deal.model.dto.*;
import ru.development.Deal.model.dto.mapper.CreditDtoMapper;
import ru.development.Deal.model.dto.mapper.LoanStatementRequestMapper;
import ru.development.Deal.model.dto.mapper.OfferDtoMapper;
import ru.development.Deal.model.dto.mapper.ScoringDataDtoMapper;
import ru.development.Deal.model.enums.*;
import ru.development.Deal.repository.ClientRepository;
import ru.development.Deal.repository.StatementRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealServiceImplTest {

    @Mock
    private LoanStatementRequestMapper loanStatementRequestMapper;
    @Mock
    private OfferDtoMapper offerDtoMapper;
    @Mock
    private ScoringDataDtoMapper scoringDataDtoMapper;
    @Mock
    private CreditDtoMapper creditDtoMapper;
    @Mock
    private StatementRepository statementRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private DealClient dealClient;
    @InjectMocks
    private DealServiceImpl dealService;

    private static LoanStatementRequestDto loanStatementRequestDto;
    private static Statement statement;
    private static Statement statementWithId;
    private static Statement statementWithIdApproved;
    List<LoanOfferDto> expectedList = new ArrayList<>();
    private static LoanOfferDto loanOfferDto;
    private static Offer offer;
    private static Statement statementWithOffer;
    private static FinishRegistrationRequestDto finishRegistrationRequestDto;
    private static ScoringDataDto scoringDataDto;
    private static CreditDto creditDto;
    private static Credit credit;
    private static Statement statementWithCredit;

    private static Client client;
    @Captor
    private ArgumentCaptor<Statement> statementArgumentCaptor;

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

        statementWithId = statement;
        statementWithId.setStatementIdUuid(UUID.fromString("c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22"));

        loanOfferDto = LoanOfferDto.builder()
                .statementIdUuid(UUID.fromString("c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22"))
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(115220.88))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(9601.74))
                .rate(BigDecimal.valueOf(27))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        offer = Offer.builder()
                .requestedAmount(loanOfferDto.getRequestedAmount())
                .totalAmount(loanOfferDto.getTotalAmount())
                .term(loanOfferDto.getTerm())
                .monthlyPayment(loanOfferDto.getMonthlyPayment())
                .rate(loanOfferDto.getRate())
                .isInsuranceEnabled(loanOfferDto.getIsInsuranceEnabled())
                .isSalaryClient(loanOfferDto.getIsSalaryClient())
                .build();

        statementWithOffer = statementWithId;
        statementWithOffer.setAppliedOffer(offer);

        statementWithIdApproved = statementWithOffer;
        statementWithIdApproved.setStatus(ApplicationStatus.APPROVED);
        List<StatusHistory> statusHistory = statementWithOffer.getStatusHistory();
        statusHistory.add(StatusHistory.builder()
                .status(ApplicationStatus.APPROVED)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build());
        statementWithIdApproved.setStatusHistory(statusHistory);

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

        credit = Credit.builder()
                .amount(creditDto.getAmount())
                .term(creditDto.getTerm())
                .monthlyPayment(creditDto.getMonthlyPayment())
                .rate(creditDto.getRate())
                .psk(creditDto.getPsk())
                .paymentSchedule(creditDto.getPaymentSchedule())
                .insuranceEnabled(creditDto.getIsInsuranceEnabled())
                .salaryClient(creditDto.getIsSalaryClient())
                .creditStatus(CreditStatus.CALCULATED)
                .build();

        statementWithCredit = statementWithIdApproved;
        statementWithCredit.setStatus(ApplicationStatus.CC_APPROVED);
        statementWithCredit.setCredit(credit);
        List<StatusHistory> updatedStatusHistory = statementWithIdApproved.getStatusHistory();
        statusHistory.add(StatusHistory.builder()
                .status(ApplicationStatus.CC_APPROVED)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build());
        statementWithCredit.setStatusHistory(updatedStatusHistory);
    }

    @Test
    void processLoanStatementTest_whenLoanStatementRequestDtoPassed_thenReturnLoanOfferDtos() {
        when(loanStatementRequestMapper.toStatement(loanStatementRequestDto)).thenReturn(statement);
        when(clientRepository.findByPassportSeriesAndPassportNumber(anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(clientRepository.findByFirstNameAndLastNameAndMiddleNameAndBirthdate(loanStatementRequestDto.getFirstName(), loanStatementRequestDto.getLastName(),
                loanStatementRequestDto.getMiddleName(), loanStatementRequestDto.getBirthdate())).thenReturn(Optional.empty());
        when(statementRepository.save(statement)).thenReturn(statementWithId);

        expectedList.add(new LoanOfferDto(statementWithId.getStatementIdUuid(), BigDecimal.valueOf(500000),
                BigDecimal.valueOf(576104.40).setScale(2, RoundingMode.HALF_UP), 12, BigDecimal.valueOf(48008.70).setScale(2, RoundingMode.HALF_UP), BigDecimal.valueOf(27),
                false, false));
        expectedList.add(new LoanOfferDto(statementWithId.getStatementIdUuid(), BigDecimal.valueOf(500000),
                BigDecimal.valueOf(573180.84), 12, BigDecimal.valueOf(47765.07), BigDecimal.valueOf(26),
                false, true));
        expectedList.add(new LoanOfferDto(statementWithId.getStatementIdUuid(), BigDecimal.valueOf(500000),
                BigDecimal.valueOf(584644.44), 12, BigDecimal.valueOf(48720.37), BigDecimal.valueOf(26),
                true, false));
        expectedList.add(new LoanOfferDto(statementWithId.getStatementIdUuid(), BigDecimal.valueOf(500000),
                BigDecimal.valueOf(581670.48), 12, BigDecimal.valueOf(48472.54), BigDecimal.valueOf(25),
                true, true));
        expectedList = expectedList.stream().sorted(Comparator.comparing(LoanOfferDto::getRate).reversed()).toList();

        when(dealClient.calculateOffers(any(LoanStatementRequestDto.class))).thenReturn(expectedList);

        List<LoanOfferDto> loanOfferDtos = dealService.processLoanStatement(loanStatementRequestDto);

        assertEquals(expectedList, loanOfferDtos);
    }

    @Test
    void processLoanStatementTest_whenClientAlreadyRegistered_thenReturnLoanOfferDtos() {
        when(loanStatementRequestMapper.toStatement(loanStatementRequestDto)).thenReturn(statement);
        when(clientRepository.findByPassportSeriesAndPassportNumber(anyString(), anyString()))
                .thenReturn(Optional.of(client));
        when(statementRepository.save(statement)).thenReturn(statementWithId);

        expectedList.add(new LoanOfferDto(statementWithId.getStatementIdUuid(), BigDecimal.valueOf(500000),
                BigDecimal.valueOf(576104.40).setScale(2, RoundingMode.HALF_UP), 12, BigDecimal.valueOf(48008.70).setScale(2, RoundingMode.HALF_UP), BigDecimal.valueOf(27),
                false, false));
        expectedList.add(new LoanOfferDto(statementWithId.getStatementIdUuid(), BigDecimal.valueOf(500000),
                BigDecimal.valueOf(573180.84), 12, BigDecimal.valueOf(47765.07), BigDecimal.valueOf(26),
                false, true));
        expectedList.add(new LoanOfferDto(statementWithId.getStatementIdUuid(), BigDecimal.valueOf(500000),
                BigDecimal.valueOf(584644.44), 12, BigDecimal.valueOf(48720.37), BigDecimal.valueOf(26),
                true, false));
        expectedList.add(new LoanOfferDto(statementWithId.getStatementIdUuid(), BigDecimal.valueOf(500000),
                BigDecimal.valueOf(581670.48), 12, BigDecimal.valueOf(48472.54), BigDecimal.valueOf(25),
                true, true));
        expectedList = expectedList.stream().sorted(Comparator.comparing(LoanOfferDto::getRate).reversed()).toList();

        when(dealClient.calculateOffers(any(LoanStatementRequestDto.class))).thenReturn(expectedList);

        List<LoanOfferDto> loanOfferDtos = dealService.processLoanStatement(loanStatementRequestDto);

        assertEquals(expectedList, loanOfferDtos);
    }

    @Test
    void processLoanStatementTest_whenCalculatorMSThrownForbiddenException_thenLoanRefusalException() {
        when(loanStatementRequestMapper.toStatement(loanStatementRequestDto)).thenReturn(statement);
        when(clientRepository.findByPassportSeriesAndPassportNumber(anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(clientRepository.findByFirstNameAndLastNameAndMiddleNameAndBirthdate(loanStatementRequestDto.getFirstName(), loanStatementRequestDto.getLastName(),
                loanStatementRequestDto.getMiddleName(), loanStatementRequestDto.getBirthdate())).thenReturn(Optional.empty());
        when(statementRepository.save(statement)).thenReturn(statementWithId);

        expectedList.add(new LoanOfferDto(statementWithId.getStatementIdUuid(), BigDecimal.valueOf(500000),
                BigDecimal.valueOf(576104.40).setScale(2, RoundingMode.HALF_UP), 12, BigDecimal.valueOf(48008.70).setScale(2, RoundingMode.HALF_UP), BigDecimal.valueOf(27),
                false, false));
        expectedList.add(new LoanOfferDto(statementWithId.getStatementIdUuid(), BigDecimal.valueOf(500000),
                BigDecimal.valueOf(573180.84), 12, BigDecimal.valueOf(47765.07), BigDecimal.valueOf(26),
                false, true));
        expectedList.add(new LoanOfferDto(statementWithId.getStatementIdUuid(), BigDecimal.valueOf(500000),
                BigDecimal.valueOf(584644.44), 12, BigDecimal.valueOf(48720.37), BigDecimal.valueOf(26),
                true, false));
        expectedList.add(new LoanOfferDto(statementWithId.getStatementIdUuid(), BigDecimal.valueOf(500000),
                BigDecimal.valueOf(581670.48), 12, BigDecimal.valueOf(48472.54), BigDecimal.valueOf(25),
                true, true));
        expectedList = expectedList.stream().sorted(Comparator.comparing(LoanOfferDto::getRate).reversed()).toList();

        when(dealClient.calculateOffers(any(LoanStatementRequestDto.class))).thenThrow(LoanRefusalException.class);

        assertThrows(LoanRefusalException.class, () -> dealService.processLoanStatement(loanStatementRequestDto));
    }

    @Test
    void selectOfferTest_whenLoanOfferDtoPassed_thenStatementUpdated() {
        when(statementRepository.findById(statementWithId.getStatementIdUuid())).thenReturn(Optional.of(statementWithId));
        when(offerDtoMapper.toOffer(loanOfferDto)).thenReturn(offer);
        when(statementRepository.save(statementWithId)).thenReturn(Optional.of(statementWithIdApproved).get());

        dealService.selectOffer(loanOfferDto);

        verify(statementRepository).save(statementArgumentCaptor.capture());
        Statement statementWithIdApproved = statementArgumentCaptor.getValue();
        verify(statementRepository, times(1)).save(statementWithIdApproved);
    }

    @Test
    void selectOfferTest_whenStatementNotFound_thenThrowNoObjectFoundException() {
        when(statementRepository.findById(statementWithId.getStatementIdUuid())).thenReturn(Optional.empty());

        assertThrows(NoObjectFoundException.class, () -> dealService.selectOffer(loanOfferDto));
    }

    @Test
    void finalizeLoanParametersTest_whenStatementExists_thenLoanParametersCalculated() {
        when(statementRepository.findById(statementWithId.getStatementIdUuid())).thenReturn(Optional.of(statementWithIdApproved));
        when(scoringDataDtoMapper.toScoringDataDto(finishRegistrationRequestDto, statementWithIdApproved)).thenReturn(scoringDataDto);
        when(dealClient.calculateCreditParameters(scoringDataDto)).thenReturn(creditDto);
        when(creditDtoMapper.toCredit(creditDto)).thenReturn(credit);
        when(statementRepository.save(statementWithCredit)).thenReturn(Optional.of(statementWithCredit).get());

        dealService.finalizeLoanParameters(finishRegistrationRequestDto, "c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22");

        verify(statementRepository).save(statementArgumentCaptor.capture());
        Statement statementForSave = statementArgumentCaptor.getValue();
        assertEquals(statementForSave, statementWithCredit);
        verify(statementRepository, times(1)).save(statementWithCredit);
    }

    @Test
    void finalizeLoanParametersTest_whenStatementNotFound_thenThrowNoObjectFoundException() {
        when(statementRepository.findById(statementWithId.getStatementIdUuid())).thenReturn(Optional.empty());

        assertThrows(NoObjectFoundException.class, () ->
                dealService.finalizeLoanParameters(finishRegistrationRequestDto, "c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22"));
    }

    @Test
    void finalizeLoanParametersTest_whenCalculatorMSThrownForbiddenException_thenLoanRefusalException() {
        when(statementRepository.findById(statementWithId.getStatementIdUuid())).thenReturn(Optional.of(statementWithIdApproved));
        when(scoringDataDtoMapper.toScoringDataDto(finishRegistrationRequestDto, statementWithIdApproved)).thenReturn(scoringDataDto);
        when(dealClient.calculateCreditParameters(scoringDataDto)).thenThrow(LoanRefusalException.class);

        assertThrows(LoanRefusalException.class, () -> {
            dealService.finalizeLoanParameters(finishRegistrationRequestDto, "c51cfb6f-39bf-4b9b-a57e-5ea8ebaf5f22");
        });
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