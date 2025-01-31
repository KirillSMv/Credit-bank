package ru.development.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.development.deal.client.interfaces.DealClient;
import ru.development.deal.error_handler.LoanRefusalException;
import ru.development.deal.error_handler.NoObjectFoundException;
import ru.development.deal.kafka.KafkaTopicsMessagesProperties;
import ru.development.deal.kafka.interfaces.DataSender;
import ru.development.deal.model.*;
import ru.development.deal.model.dto.*;
import ru.development.deal.model.dto.mapper.CreditDtoMapper;
import ru.development.deal.model.dto.mapper.LoanStatementRequestMapper;
import ru.development.deal.model.dto.mapper.OfferDtoMapper;
import ru.development.deal.model.dto.mapper.ScoringDataDtoMapper;
import ru.development.deal.model.enums.ApplicationStatus;
import ru.development.deal.model.enums.ChangeType;
import ru.development.deal.model.enums.CreditStatus;
import ru.development.deal.model.enums.Theme;
import ru.development.deal.repository.ClientRepository;
import ru.development.deal.repository.StatementRepository;
import ru.development.deal.service.interfaces.DealService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
    private final DataSender dataSender;
    private final KafkaTopicsMessagesProperties messageProperties;

    private static final String NO_STATEMENT_MESSAGE = "Заявки с таким id не найдено, id = {}";
    private static final String NO_CREDIT_STATEMENT_MESSAGE = "Заявки на кредит с такими данными не найдено";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

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
                    log.warn(NO_STATEMENT_MESSAGE, dto.getStatementIdUuid());
                    return new NoObjectFoundException(NO_CREDIT_STATEMENT_MESSAGE);
                }
        );
        statement.setStatus(ApplicationStatus.APPROVED);
        updateStatusHistory(statement, ApplicationStatus.APPROVED);
        Offer offer = offerDtoMapper.toOffer(dto);
        statement.setAppliedOffer(offer);
        statementRepository.save(statement);
        log.debug("обновлен статус заявки, новый статус - {}", statement.getStatus());
        dataSender.send(messageProperties.getFinishRegistrationTopic(), new EmailMessageDto(statement.getStatementIdUuid(),
                statement.getClient().getEmail(), Theme.FINISH_REGISTRATION, messageProperties.getFinishRegistrationMessage()));
    }

    @Override
    @Transactional(noRollbackFor = LoanRefusalException.class)
    public void finalizeLoanParameters(FinishRegistrationRequestDto dto, String statementId) {
        Statement statement = statementRepository.findById(UUID.fromString(statementId)).orElseThrow(() -> {
                    log.warn(NO_STATEMENT_MESSAGE, statementId);
                    return new NoObjectFoundException(NO_CREDIT_STATEMENT_MESSAGE);
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
            dataSender.send(messageProperties.getStatementDeniedTopic(), new EmailMessageDto(statement.getStatementIdUuid(),
                    statement.getClient().getEmail(),
                    Theme.STATEMENT_DENIED, messageProperties.getStatementDeniedMessage()));
            throw new LoanRefusalException(NO_CREDIT_STATEMENT_MESSAGE);
        }
        Credit credit = creditDtoMapper.toCredit(creditDto);
        statement.setCredit(credit);
        statement.setStatus(ApplicationStatus.CC_APPROVED);
        updateStatusHistory(statement, ApplicationStatus.CC_APPROVED);
        statementRepository.save(statement);
        dataSender.send(messageProperties.getCreateDocumentsTopic(), new EmailMessageDto(statement.getStatementIdUuid(),
                statement.getClient().getEmail(), Theme.CREATE_DOCUMENTS, messageProperties.getCreateDocumentsMessage() +
                String.format(messageProperties.getDealServerUrl().concat(messageProperties.getUrlRequestDocuments()), statement.getStatementIdUuid())));
        log.debug("обновлена заявка с id {}, новый статус - {}, добавлен рассчитанный кредит с id {}", statement.getStatementIdUuid(),
                statement.getStatus(), statement.getCredit().getCreditIdUuid());
    }

    @Override
    @Transactional
    public void sendDocuments(String statementId) {
        Statement statement = statementRepository.findById(UUID.fromString(statementId)).orElseThrow(() -> {
                    log.warn(NO_STATEMENT_MESSAGE, statementId);
                    return new NoObjectFoundException(NO_CREDIT_STATEMENT_MESSAGE);
                }
        );
        statement.setStatus(ApplicationStatus.PREPARE_DOCUMENTS);
        updateStatusHistory(statement, ApplicationStatus.PREPARE_DOCUMENTS);
        statementRepository.save(statement);
        log.debug("обновлена заявка с id {}, новый статус - {}", statement.getStatementIdUuid(), statement.getStatus());
        dataSender.send(messageProperties.getSendDocumentsTopic(), new EmailMessageDto(statement.getStatementIdUuid(), statement.getClient().getEmail(),
                Theme.SEND_DOCUMENTS, messageProperties.getSendDocumentsMessage() +
                String.format(messageProperties.getDealServerUrl().concat(messageProperties.getUrlSignDocuments()), statement.getStatementIdUuid())));
    }


    @Override
    @Transactional
    public void signDocuments(String statementId) {
        Statement statement = statementRepository.findById(UUID.fromString(statementId)).orElseThrow(() -> {
                    log.warn(NO_STATEMENT_MESSAGE, statementId);
                    return new NoObjectFoundException(NO_CREDIT_STATEMENT_MESSAGE);
                }
        );
        statement.setSes(generateSesCode());
        statementRepository.save(statement);
        log.debug("обновлена заявка с id {}, добавлен ses код", statement.getStatementIdUuid());
        dataSender.send(messageProperties.getSendSesCodeTopic(), new EmailMessageDto(statement.getStatementIdUuid(), statement.getClient().getEmail(),
                Theme.SEND_SES_CODE, messageProperties.getSendSesCodeMessage() + statement.getSes() +
                String.format(messageProperties.getDealServerUrl().concat(messageProperties.getUrlProcessSesCode()), statement.getStatementIdUuid())));
    }

    @Override
    @Transactional(noRollbackFor = LoanRefusalException.class)
    public void processSesCode(String statementId, String code) {
        Statement statement = statementRepository.findById(UUID.fromString(statementId)).orElseThrow(() -> {
                    log.warn(NO_STATEMENT_MESSAGE, statementId);
                    return new NoObjectFoundException(NO_CREDIT_STATEMENT_MESSAGE);
                }
        );
        statement.setStatus(ApplicationStatus.DOCUMENT_SIGNED);
        statement.setSignDate(LocalDateTime.now());
        updateStatusHistory(statement, ApplicationStatus.DOCUMENT_SIGNED);
        if (statement.getSes().equals(code)) {
            statement.setStatus(ApplicationStatus.CREDIT_ISSUED);
            updateStatusHistory(statement, ApplicationStatus.CREDIT_ISSUED);
            Credit credit = statement.getCredit();
            credit.setCreditStatus(CreditStatus.ISSUED);
            statement.setCredit(credit);
            statementRepository.save(statement);
            dataSender.send(messageProperties.getCreditIssuedTopic(), new EmailMessageDto(statement.getStatementIdUuid(), statement.getClient().getEmail(),
                    Theme.CREDIT_ISSUED, messageProperties.getCreditIssuedMessage()));
        } else {
            statement.setStatus(ApplicationStatus.CLIENT_DENIED);
            updateStatusHistory(statement, ApplicationStatus.CLIENT_DENIED);
            statementRepository.flush();
            throw new LoanRefusalException(String.format("Пожалуйста, проверьте правильность переданного ses кода, " +
                    "переданное значение - %s", code));
        }
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

    private String generateSesCode() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            stringBuilder.append(SECURE_RANDOM.nextInt(10));
        }
        return stringBuilder.toString();
    }
}