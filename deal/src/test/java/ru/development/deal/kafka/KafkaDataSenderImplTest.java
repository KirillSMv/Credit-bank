package ru.development.deal.kafka;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ru.development.deal.model.dto.EmailMessageDto;
import ru.development.deal.model.enums.Theme;

import java.util.UUID;

@SpringBootTest(classes = {KafkaConsumerTestConfig.class})
@EmbeddedKafka(partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:9093", "port=9093"}, topics = "finish-registration")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class KafkaDataSenderImplTest {

    @Autowired
    private KafkaDataSenderImpl dataSender;

    @KafkaListener(topics = "finish-registration", groupId = "test-group")
    public boolean listen(EmailMessageDto message) {
        return "FINISH_REGISTRATION".equals(message.getTheme().toString()) && "63e8f05d-6d53-49b0-a4f5-80939b31a0d1".equals(message.getStatementId().toString());
    }

    @Test
    void send() {
        EmailMessageDto dto = new EmailMessageDto(UUID.fromString("63e8f05d-6d53-49b0-a4f5-80939b31a0d1"), "kirill.mamontov.test@gmail.com", Theme.FINISH_REGISTRATION,
                "FINISH_REGISTRATION");
        dataSender.send("finish-registration", dto);
        Assertions.assertTrue(listen(dto));
    }
}