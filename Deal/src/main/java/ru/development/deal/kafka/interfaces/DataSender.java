package ru.development.deal.kafka.interfaces;

import ru.development.deal.model.dto.EmailMessageDto;

public interface DataSender {

    void send(String topic, EmailMessageDto value);
}
