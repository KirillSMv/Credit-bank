package ru.development.Deal.kafka.interfaces;

import ru.development.Deal.model.dto.EmailMessageDto;

public interface DataSender {

    void send(String topic, EmailMessageDto value);
}
