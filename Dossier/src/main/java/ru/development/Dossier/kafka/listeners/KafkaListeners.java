package ru.development.Dossier.kafka.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.development.Dossier.kafka.KafkaTopicsMessagesProperties;
import ru.development.Dossier.model.EmailMessageDto;

@Component
@RequiredArgsConstructor
public class KafkaListeners {
    private final KafkaTopicsMessagesProperties properties;


    @KafkaListener(
            topics = "finish-registration",
            groupId = "dossier-consumer"
    )
    void listenFinishRegistrationTopic(EmailMessageDto message) {
        System.out.println("finish-registration received" + message);
    }

    @KafkaListener(
            topics = "create-documents",
            groupId = "dossier-consumer"
    )
    void listenCreateDocumentsTopic(EmailMessageDto message) {
        System.out.println("create-documents received" + message);
    }

    @KafkaListener(
            topics = "send-documents",
            groupId = "dossier-consumer"
    )
    void listenSendDocumentsTopic(EmailMessageDto message) {
        System.out.println("send-documents received" + message);
    }

    @KafkaListener(
            topics = "send-ses",
            groupId = "dossier-consumer"
    )
    void listenSendSesTopic(EmailMessageDto message) {
        System.out.println("send-ses received" + message);
    }

    @KafkaListener(
            topics = "credit-issued",
            groupId = "dossier-consumer"
    )
    void listenCreditIssuedTopic(EmailMessageDto message) {
        System.out.println("credit-issued received" + message);
    }

    @KafkaListener(
            topics = "statement-denied",
            groupId = "dossier-consumer"
    )
    void listenStatementDeniedTopic(EmailMessageDto message) {
        System.out.println("statement-denied received" + message);
    }
}
