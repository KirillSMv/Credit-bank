package ru.development.Dossier.kafka.listeners;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.development.Dossier.client.ClientService;
import ru.development.Dossier.mail_service.MailProperties;
import ru.development.Dossier.mail_service.MailService;
import ru.development.Dossier.model.EmailMessageDto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class KafkaListeners {
    private final ClientService clientService;
    private final MailService mailService;
    private final MailProperties mailProperties;
    private String regex = "http\\S*";
    private Pattern pattern = Pattern.compile(regex);
    String htmlWithPostForm = "<html>" +
            "<body>" +
            "<p>%s</p>" +
            "<form action=" + "'%s'" + " method='POST'>" +
            "<input type='submit' value='Ваша ссылка'>" +
            "</form>" +
            "</body>" +
            "</html>";

    @KafkaListener(
            topics = "${kafka.finish-registration-topic}",
            groupId = "dossier-consumer"
    )
    void listenFinishRegistrationTopic(EmailMessageDto message) {
        mailService.send(message.getAddress(), message.getText(), mailProperties.getFinishRegistrationSubjectLine());
    }

    @KafkaListener(
            topics = "${kafka.create-documents-topic}",
            groupId = "dossier-consumer"
    )
    void listenCreateDocumentsTopic(EmailMessageDto message) throws MessagingException {
        System.out.println("create-documents received" + message);

        Matcher matcher = pattern.matcher(message.getText());
        String text = String.join(" ", message.getText().split(regex));
        if (matcher.find()) {
            String url = matcher.group();
            String personalizedHtml = String.format(htmlWithPostForm, text, url);
            mailService.sendHtml(message.getAddress(), personalizedHtml, mailProperties.getFinishRegistrationSubjectLine());
        } else {
            throw new RuntimeException("Пожалуйста, попробуйте отправить запрос позднее");
        }
    }

    @KafkaListener(
            topics = "${kafka.send-documents-topic}",
            groupId = "dossier-consumer"
    )
    void listenSendDocumentsTopic(EmailMessageDto message) throws MessagingException {
        clientService.send(String.valueOf(message.getStatementId()));
        System.out.println("send-documents received" + message);

        Matcher matcher = pattern.matcher(message.getText());
        String text = String.join(" ", message.getText().split(regex));
        if (matcher.find()) {
            String url = matcher.group();
            String personalizedHtml = String.format(htmlWithPostForm, text, url);
            mailService.sendHtml(message.getAddress(), personalizedHtml, mailProperties.getSendDocumentsSubjectLine());
        } else {
            throw new RuntimeException("Пожалуйста, попробуйте отправить запрос позднее");
        }
    }

    @KafkaListener(
            topics = "${kafka.send-sesCode-topic}",
            groupId = "dossier-consumer"
    )
    void listenSendSesTopic(EmailMessageDto message) throws MessagingException {
        System.out.println("send-ses received" + message);

        Matcher matcher = pattern.matcher(message.getText());
        String text = String.join(" ", message.getText().split(regex));
        if (matcher.find()) {
            String url = matcher.group();
            String htmlPostWithCode = "<html>" +
                    "<body>" +
                    "<p>" + text + "</p>" +
                    "<form action=" + "'" + url + "'" + " method='POST'>" +
                    "<label for='name'>Ses-код:</label><br>" +
                    "<input type='text' id='name' name='code' required><br><br>" +
                    "<button type='submit'>Отправить код</button>" +
                    "</form>" +
                    "</body>" +
                    "</html>";
            mailService.sendHtml(message.getAddress(), htmlPostWithCode, mailProperties.getSendSesCodeSubjectLine());
        } else {
            throw new RuntimeException("Пожалуйста, попробуйте отправить запрос позднее");
        }
    }

    @KafkaListener(
            topics = "${kafka.credit-issued-topic}",
            groupId = "dossier-consumer"
    )
    void listenCreditIssuedTopic(EmailMessageDto message) {
        System.out.println("credit-issued received" + message);
        mailService.send(message.getAddress(), message.getText(), mailProperties.getCreditIssuedSubjectLine());
    }

    @KafkaListener(
            topics = "${kafka.statement-denied-topic}",
            groupId = "dossier-consumer"
    )
    void listenStatementDeniedTopic(EmailMessageDto message) {
        System.out.println("statement-denied received" + message);
        mailService.send(message.getAddress(), message.getText(), mailProperties.getStatementDeniedSubjectLine());
    }
}
