package ru.development.deal.kafka;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import ru.development.deal.model.dto.EmailMessageDto;
import ru.development.deal.model.enums.Theme;

import java.util.UUID;

@SpringBootTest(classes = {KafkaConsumerTestConfig.class})
@EmbeddedKafka(partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"}, topics = "finish-registration")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class KafkaDataSenderImplTest {

    @Autowired
    private KafkaDataSenderImpl dataSender;

    @KafkaListener(topics = "finish-registration", groupId = "test-group")
    public void listen(EmailMessageDto message) {
        Assertions.assertEquals("FINISH_REGISTRATION", message.getTheme());
        Assertions.assertEquals("63e8f05d-6d53-49b0-a4f5-80939b31a0d1", message.getStatementId());
    }

    @Test
    void send() {
        EmailMessageDto dto = new EmailMessageDto(UUID.fromString("63e8f05d-6d53-49b0-a4f5-80939b31a0d1"), "kirill.mamontov.test@gmail.com", Theme.FINISH_REGISTRATION,
                "FINISH_REGISTRATION");
        dataSender.send("finish-registration", dto);
    }
}