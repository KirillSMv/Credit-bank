package ru.development.Deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.development.Deal.client.DealClient;
import ru.development.Deal.error_handler.LoanRefusalException;
import ru.development.Deal.error_handler.NoObjectFoundException;
import ru.development.Deal.model.*;
import ru.development.Deal.model.dto.*;
import ru.development.Deal.model.dto.mapper.CreditDtoMapper;
import ru.development.Deal.model.dto.mapper.LoanStatementRequestMapper;
import ru.development.Deal.model.dto.mapper.OfferDtoMapper;
import ru.development.Deal.model.dto.mapper.ScoringDataDtoMapper;
import ru.development.Deal.model.enums.ApplicationStatus;
import ru.development.Deal.model.enums.ChangeType;
import ru.development.Deal.repository.ClientRepository;
import ru.development.Deal.repository.StatementRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DealServiceImpl implements DealService {
    private final LoanStatementRequestMapper loanStatementRequestMapper;
    private final OfferDtoMapper offerDtoMapper;
    private final ScoringDataDtoMapper scoringDataDtoMapper;
    private final CreditDtoMapper creditDtoMapper;
    private final StatementRepository statementRepository;
    private final ClientRepository clientRepository;
    private final DealClient dealClient;

    @Override
    @Transactional(noRollbackFor = LoanRefusalException.class)
    public List<LoanOfferDto> processLoanStatement(LoanStatementRequestDto dto) {
        Statement statement = loanStatementRequestMapper.toStatement(dto);
        checkIfClientAlreadyExists(dto, statement);
        Statement savedStatement = statementRepository.save(statement);
        log.debug("сохранена заявка c id = {}", savedStatement.getStatementIdUuid());
        List<LoanOfferDto> loanOfferDtos;
        try {
            loanOfferDtos = dealClient.calculateOffers(dto);
        } catch (LoanRefusalException exception) {
            log.debug("Выброшено исключение сервером - {}", exception.getMessage());
            statement.setStatus(ApplicationStatus.DENIED);
            updateStatusHistory(statement, ApplicationStatus.DENIED);
            statementRepository.flush();
            throw new LoanRefusalException("К сожалению, вам отказано в кредите");
        }
        loanOfferDtos.forEach(offer -> offer.setStatementIdUuid(savedStatement.getStatementIdUuid()));
        return loanOfferDtos;
    }

    @Override
    public void selectOffer(LoanOfferDto dto) {
        Statement statement = statementRepository.findById(dto.getStatementIdUuid()).orElseThrow(() -> {
                    log.warn("Заявки с таким id не найдено, id = {}", dto.getStatementIdUuid());
                    return new NoObjectFoundException("Заявки на кредит с такими данными не найдено");
                }
        );
        statement.setStatus(ApplicationStatus.APPROVED);
        updateStatusHistory(statement, ApplicationStatus.APPROVED);
        Offer offer = offerDtoMapper.toOffer(dto);
        statement.setAppliedOffer(offer);
        statementRepository.save(statement);
        log.debug("обновлен статус заявки, новый статус - {}", statement.getStatus());
    }

    @Override
    @Transactional(noRollbackFor = LoanRefusalException.class)
    public void finalizeLoanParameters(FinishRegistrationRequestDto dto, String statementId) {
        Statement statement = statementRepository.findById(UUID.fromString(statementId)).orElseThrow(() -> {
                    log.warn("Заявки с таким id не найдено, id = {}", statementId);
                    return new NoObjectFoundException("Заявки на кредит с такими данными не найдено");
                }
        );
        ScoringDataDto scoringDataDto = scoringDataDtoMapper.toScoringDataDto(dto, statement);
        CreditDto creditDto;
        try {
            creditDto = dealClient.calculateCreditParameters(scoringDataDto);
        } catch (LoanRefusalException exception) {
            log.debug("Выброшено исключение сервером - {}", exception.getMessage());
            statement.setStatus(ApplicationStatus.CC_DENIED);
            updateStatusHistory(statement, ApplicationStatus.CC_DENIED);
            statementRepository.flush();
            throw new LoanRefusalException("К сожалению, вам отказано в кредите");
        }
        Credit credit = creditDtoMapper.toCredit(creditDto);
        statement.setCredit(credit);
        statement.setStatus(ApplicationStatus.CC_APPROVED);
        updateStatusHistory(statement, ApplicationStatus.CC_APPROVED);
        statementRepository.save(statement);
        log.debug("обновлена заявка с id {}, новый статус - {}, добавлен рассчитанный кредит с id {}", statement.getStatementIdUuid(),
                statement.getStatus(), statement.getCredit().getCreditIdUuid());
    }

    private void checkIfClientAlreadyExists(LoanStatementRequestDto dto, Statement statement) {
        Optional<Client> clientByPassport = clientRepository.findByPassportSeriesAndPassportNumber(dto.getPassportSeries(),
                dto.getPassportNumber());
        log.debug("clientByPassport == null: {}", clientByPassport.isPresent());
        Optional<Client> clientByOtherData = clientRepository.findByFirstNameAndLastNameAndMiddleNameAndBirthdate(dto.getFirstName(),
                dto.getLastName(), dto.getMiddleName(),
                dto.getBirthdate());
        log.debug("clientByOtherData == null: {}", clientByOtherData.isPresent());
        if (clientByPassport.isPresent()) {
            Client existingClient = clientByPassport.get();
            updateAllFields(dto, existingClient);
            statement.setClient(existingClient);
        }
        if (clientByOtherData.isPresent()) {
            Client existingClient = clientByOtherData.get();
            updateAllFields(dto, existingClient);
            statement.setClient(existingClient);
        }
    }

    private void updateAllFields(LoanStatementRequestDto dto, Client existingClient) {
        existingClient.setFirstName(dto.getFirstName());
        existingClient.setLastName(dto.getLastName());
        existingClient.setMiddleName(dto.getMiddleName());
        existingClient.setBirthdate(dto.getBirthdate());
        existingClient.setBirthdate(dto.getBirthdate());
        existingClient.setEmail(dto.getEmail());
        existingClient.setPassport(Passport.builder().series(dto.getPassportSeries()).number(dto.getPassportNumber()).build());
    }

    private void updateStatusHistory(Statement statement, ApplicationStatus applicationStatus) {
        List<StatusHistory> statusHistory = statement.getStatusHistory();
        statusHistory.add(StatusHistory.builder()
                .status(applicationStatus)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build());
        statement.setStatusHistory(statusHistory);
    }
}
