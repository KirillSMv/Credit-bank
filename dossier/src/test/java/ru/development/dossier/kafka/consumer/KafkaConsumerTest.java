package ru.development.dossier.kafka.consumer;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import ru.development.dossier.model.EmailMessageDto;
import ru.development.dossier.model.Theme;

import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = {KafkaProducerTestConfig.class})
@EmbeddedKafka(partitions = 1,
        brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class KafkaConsumerTest {
    @Autowired
    private KafkaTemplate<String, EmailMessageDto> kafkaTemplate;

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Mock
    Acknowledgment acknowledgment;

    @Test
    void consumeFinishRegistrationTopic() throws InterruptedException {
        EmailMessageDto dto = new EmailMessageDto(UUID.fromString("63e8f05d-6d53-49b0-a4f5-80939b31a0d1"), "kirill.mamontov.test@gmail.com", Theme.FINISH_REGISTRATION,
                "FINISH_REGISTRATION");
        kafkaTemplate.send("finish-registration", dto);

        kafkaConsumer.consumeFinishRegistrationTopic(dto, acknowledgment);
        verify(acknowledgment, times(1)).acknowledge();
    }
    @Test
    void consumeCreateDocumentsTopic() {
        EmailMessageDto dto = new EmailMessageDto(UUID.fromString("63e8f05d-6d53-49b0-a4f5-80939b31a0d1"), "kirill.mamontov.test@gmail.com", Theme.CREATE_DOCUMENTS,
                "CREATE_DOCUMENTS");
        kafkaTemplate.send("create-documents", dto);

        kafkaConsumer.consumeFinishRegistrationTopic(dto, acknowledgment);
        verify(acknowledgment, times(1)).acknowledge();
    }

    @Test
    void consumeSendDocumentsTopic() {
        EmailMessageDto dto = new EmailMessageDto(UUID.fromString("63e8f05d-6d53-49b0-a4f5-80939b31a0d1"), "kirill.mamontov.test@gmail.com", Theme.SEND_DOCUMENTS,
                "SEND_DOCUMENTS");
        kafkaTemplate.send("send-documents", dto);

        kafkaConsumer.consumeFinishRegistrationTopic(dto, acknowledgment);
        verify(acknowledgment, times(1)).acknowledge();
    }

    @Test
    void consumeSendSesTopic() {
        EmailMessageDto dto = new EmailMessageDto(UUID.fromString("63e8f05d-6d53-49b0-a4f5-80939b31a0d1"), "kirill.mamontov.test@gmail.com", Theme.SEND_SES_CODE,
                "SEND_SES_CODE");
        kafkaTemplate.send("send-ses", dto);

        kafkaConsumer.consumeFinishRegistrationTopic(dto, acknowledgment);
        verify(acknowledgment, times(1)).acknowledge();
    }

    @Test
    void consumeCreditIssuedTopic() {
        EmailMessageDto dto = new EmailMessageDto(UUID.fromString("63e8f05d-6d53-49b0-a4f5-80939b31a0d1"), "kirill.mamontov.test@gmail.com", Theme.CREDIT_ISSUED,
                "CREDIT_ISSUED");
        kafkaTemplate.send("credit-issued", dto);

        kafkaConsumer.consumeFinishRegistrationTopic(dto, acknowledgment);
        verify(acknowledgment, times(1)).acknowledge();
    }

    @Test
    void consumeStatementDeniedTopic() {
        EmailMessageDto dto = new EmailMessageDto(UUID.fromString("63e8f05d-6d53-49b0-a4f5-80939b31a0d1"), "kirill.mamontov.test@gmail.com", Theme.STATEMENT_DENIED,
                "STATEMENT_DENIED");
        kafkaTemplate.send("statement-denied", dto);

        kafkaConsumer.consumeFinishRegistrationTopic(dto, acknowledgment);
        verify(acknowledgment, times(1)).acknowledge();
    }
}