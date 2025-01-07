package ru.development.Dossier.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaTopicsMessagesProperties {
    private String finishRegistrationTopic;
    private String createDocumentsTopic;
    private String sendDocumentsTopic;
    private String sendSesCodeTopic;
    private String creditIssuedTopic;
    private String statementDeniedTopic;
    private String finishRegistrationMessage;
    private String createDocumentsMessage;
    private String sendDocumentsMessage;
    private String sendSesCodeMessage;
    private String creditIssuedMessage;
    private String statementDeniedMessage;
}
