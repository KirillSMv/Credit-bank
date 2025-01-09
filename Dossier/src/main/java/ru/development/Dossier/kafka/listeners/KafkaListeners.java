package ru.development.Dossier.kafka.listeners;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.development.Dossier.client.interfaces.ClientService;
import ru.development.Dossier.mail_service.MailProperties;
import ru.development.Dossier.mail_service.interfaces.MailService;
import ru.development.Dossier.model.EmailMessageDto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaListeners {
    private final ClientService clientService;
    private final MailService mailService;
    private final MailProperties mailProperties;
    private String regex = "http\\S*";
    private Pattern pattern = Pattern.compile(regex);

    @KafkaListener(
            topics = "${kafka.finish-registration-topic}",
            groupId = "dossier-consumer"
    )
    void listenFinishRegistrationTopic(EmailMessageDto message) {
        log.debug("Получено сообщение, тема письма: {}, id заявки: {}", message.getTheme(), message.getStatementId());
        mailService.send(message.getAddress(), message.getText(), mailProperties.getFinishRegistrationSubjectLine());
    }

    @KafkaListener(
            topics = "${kafka.create-documents-topic}",
            groupId = "dossier-consumer"
    )
    void listenCreateDocumentsTopic(EmailMessageDto message) throws MessagingException {
        log.debug("Получено сообщение, тема письма: {}, id заявки: {}", message.getTheme(), message.getStatementId());
        Matcher matcher = pattern.matcher(message.getText());
        String text = String.join(" ", message.getText().split(regex));
        if (matcher.find()) {
            String url = matcher.group();
            String personalizedHtml = String.format(mailProperties.getHtmlWithPostForm(), text, url);
            mailService.sendHtml(message.getAddress(), personalizedHtml, mailProperties.getFinishRegistrationSubjectLine());
        } else {
            log.warn("в теле сообщения не найдено ссылки, выброшено исключение");
            throw new RuntimeException("Пожалуйста, попробуйте отправить запрос позднее");
        }
    }

    @KafkaListener(
            topics = "${kafka.send-documents-topic}",
            groupId = "dossier-consumer"
    )
    void listenSendDocumentsTopic(EmailMessageDto message) throws MessagingException {
        log.debug("Получено сообщение, тема письма: {}, id заявки: {}", message.getTheme(), message.getStatementId());
        clientService.send(String.valueOf(message.getStatementId()));
        Matcher matcher = pattern.matcher(message.getText());
        String text = String.join(" ", message.getText().split(regex));
        if (matcher.find()) {
            String url = matcher.group();
            String personalizedHtml = String.format(mailProperties.getHtmlWithPostForm(), text, url);
            mailService.sendHtml(message.getAddress(), personalizedHtml, mailProperties.getSendDocumentsSubjectLine());
        } else {
            log.warn("В теле сообщения не найдено ссылки, выброшено исключение");
            throw new RuntimeException("Пожалуйста, попробуйте отправить запрос позднее");
        }
    }

    @KafkaListener(
            topics = "${kafka.send-sesCode-topic}",
            groupId = "dossier-consumer"
    )
    void listenSendSesTopic(EmailMessageDto message) throws MessagingException {
        log.debug("Получено сообщение, тема письма: {}, id заявки: {}", message.getTheme(), message.getStatementId());
        Matcher matcher = pattern.matcher(message.getText());
        String text = String.join(" ", message.getText().split(regex));
        if (matcher.find()) {
            String url = matcher.group();
            String urlPostWithCode = String.format(mailProperties.getHtmlPostWithCode(), text, url);
            mailService.sendHtml(message.getAddress(), urlPostWithCode, mailProperties.getSendSesCodeSubjectLine());
        } else {
            log.warn("В теле сообщения не найдено ссылки, выброшено исключение");
            throw new RuntimeException("Пожалуйста, попробуйте отправить запрос позднее");
        }
    }

    @KafkaListener(
            topics = "${kafka.credit-issued-topic}",
            groupId = "dossier-consumer"
    )
    void listenCreditIssuedTopic(EmailMessageDto message) {
        log.debug("Получено сообщение, тема письма: {}, id заявки: {}", message.getTheme(), message.getStatementId());
        mailService.send(message.getAddress(), message.getText(), mailProperties.getCreditIssuedSubjectLine());
    }

    @KafkaListener(
            topics = "${kafka.statement-denied-topic}",
            groupId = "dossier-consumer"
    )
    void listenStatementDeniedTopic(EmailMessageDto message) {
        log.debug("Получено сообщение, тема письма: {}, id заявки: {}", message.getTheme(), message.getStatementId());
        mailService.send(message.getAddress(), message.getText(), mailProperties.getStatementDeniedSubjectLine());
    }
}
