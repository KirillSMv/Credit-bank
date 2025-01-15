package ru.development.Deal.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.development.Deal.kafka.interfaces.DataSender;
import ru.development.Deal.model.dto.EmailMessageDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataSenderImpl implements DataSender {

    private final KafkaTemplate<String, EmailMessageDto> kafkaTemplate;

    @Override
    public void send(String topic, EmailMessageDto message) {
        kafkaTemplate.send(topic, message)
                .whenComplete(
                        (result, ex) -> {
                            if (ex == null) {
                                log.debug("сообщение с id заявки {} было отправлено успешно, offset ={}",
                                        message.getStatementId(), result.getRecordMetadata().offset());
                            } else {
                                log.warn("сообщение с id заявки {} не отправлено",
                                        message.getStatementId(), ex);
                            }
                        });
    }
}
