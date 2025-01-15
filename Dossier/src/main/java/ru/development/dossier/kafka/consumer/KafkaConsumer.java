package ru.development.dossier.kafka.consumer;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import ru.development.dossier.client.interfaces.ClientService;
import ru.development.dossier.error_handler.RequestProcessingException;
import ru.development.dossier.mail_service.MailProperties;
import ru.development.dossier.mail_service.interfaces.MailService;
import ru.development.dossier.model.EmailMessageDto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {
    private final ClientService clientService;
    private final MailService mailService;
    private final MailProperties mailProperties;
    private static final String LOGGING_MESSAGE_RECEIVED = "Получено сообщение, тема письма: {}, id заявки: {}";
    private static final String LOGGING_MESSAGE_PROCESSED = "Сообщение успешно обработано";
    private static final String LOGGING_MESSAGE_FAIL_NO_LINK = "В теле сообщения не найдено ссылки, выброшено исключение";
    private static final String LOGGING_SEND_RESPONSE_LATER = "Пожалуйста, попробуйте отправить запрос позднее";
    private String regex = "http\\S*";
    private Pattern pattern = Pattern.compile(regex);

    @KafkaListener(
            topics = "${kafka.finish-registration-topic}",
            groupId = "dossier-consumer"
    )
    void consumeFinishRegistrationTopic(EmailMessageDto message, Acknowledgment acknowledgment) {
        log.debug(LOGGING_MESSAGE_RECEIVED, message.getTheme(), message.getStatementId());
        mailService.send(message.getAddress(), message.getText(), mailProperties.getFinishRegistrationSubjectLine());
        log.debug(LOGGING_MESSAGE_PROCESSED);
        acknowledgment.acknowledge();
    }

    @KafkaListener(
            topics = "${kafka.create-documents-topic}",
            groupId = "dossier-consumer"
    )
    void consumeCreateDocumentsTopic(EmailMessageDto message, Acknowledgment acknowledgment) throws MessagingException {
        log.debug(LOGGING_MESSAGE_RECEIVED, message.getTheme(), message.getStatementId());
        Matcher matcher = pattern.matcher(message.getText());
        String text = String.join(" ", message.getText().split(regex));
        if (matcher.find()) {
            String url = matcher.group();
            String personalizedHtml = String.format(mailProperties.getHtmlWithPostForm(), text, url);
            mailService.sendHtml(message.getAddress(), personalizedHtml, mailProperties.getFinishRegistrationSubjectLine());
            log.debug(LOGGING_MESSAGE_PROCESSED);
            acknowledgment.acknowledge();
        } else {
            log.warn(LOGGING_MESSAGE_FAIL_NO_LINK);
            throw new RequestProcessingException(LOGGING_SEND_RESPONSE_LATER);
        }

    }

    @KafkaListener(
            topics = "${kafka.send-documents-topic}",
            groupId = "dossier-consumer"
    )
    void consumeSendDocumentsTopic(EmailMessageDto message, Acknowledgment acknowledgment) throws MessagingException {
        log.debug(LOGGING_MESSAGE_RECEIVED, message.getTheme(), message.getStatementId());
        clientService.send(String.valueOf(message.getStatementId()));
        Matcher matcher = pattern.matcher(message.getText());
        String text = String.join(" ", message.getText().split(regex));
        if (matcher.find()) {
            String url = matcher.group();
            String personalizedHtml = String.format(mailProperties.getHtmlWithPostForm(), text, url);
            mailService.sendHtml(message.getAddress(), personalizedHtml, mailProperties.getSendDocumentsSubjectLine());
            log.debug(LOGGING_MESSAGE_PROCESSED);
            acknowledgment.acknowledge();
        } else {
            log.warn(LOGGING_MESSAGE_FAIL_NO_LINK);
            throw new RequestProcessingException(LOGGING_SEND_RESPONSE_LATER);
        }
    }

    @KafkaListener(
            topics = "${kafka.send-sesCode-topic}",
            groupId = "dossier-consumer"
    )
    void consumeSendSesTopic(EmailMessageDto message, Acknowledgment acknowledgment) throws MessagingException {
        log.debug(LOGGING_MESSAGE_RECEIVED, message.getTheme(), message.getStatementId());
        Matcher matcher = pattern.matcher(message.getText());
        String text = String.join(" ", message.getText().split(regex));
        if (matcher.find()) {
            String url = matcher.group();
            String urlPostWithCode = String.format(mailProperties.getHtmlPostWithCode(), text, url);
            mailService.sendHtml(message.getAddress(), urlPostWithCode, mailProperties.getSendSesCodeSubjectLine());
            log.debug(LOGGING_MESSAGE_PROCESSED);
            acknowledgment.acknowledge();
        } else {
            log.warn(LOGGING_MESSAGE_FAIL_NO_LINK);
            throw new RequestProcessingException(LOGGING_SEND_RESPONSE_LATER);
        }
    }

    @KafkaListener(
            topics = "${kafka.credit-issued-topic}",
            groupId = "dossier-consumer"
    )
    void consumeCreditIssuedTopic(EmailMessageDto message, Acknowledgment acknowledgment) {
        log.debug(LOGGING_MESSAGE_RECEIVED, message.getTheme(), message.getStatementId());
        mailService.send(message.getAddress(), message.getText(), mailProperties.getCreditIssuedSubjectLine());
        log.debug(LOGGING_MESSAGE_PROCESSED);
        acknowledgment.acknowledge();
    }

    @KafkaListener(
            topics = "${kafka.statement-denied-topic}",
            groupId = "dossier-consumer"
    )
    void consumeStatementDeniedTopic(EmailMessageDto message, Acknowledgment acknowledgment) {
        log.debug(LOGGING_MESSAGE_RECEIVED, message.getTheme(), message.getStatementId());
        mailService.send(message.getAddress(), message.getText(), mailProperties.getStatementDeniedSubjectLine());
        log.debug(LOGGING_MESSAGE_PROCESSED);
        acknowledgment.acknowledge();
    }
}
