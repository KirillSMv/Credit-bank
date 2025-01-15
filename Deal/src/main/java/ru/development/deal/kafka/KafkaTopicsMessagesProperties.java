package ru.development.deal.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class KafkaTopicsMessagesProperties {
    @Value("${kafka.finishRegistrationTopic}")
    private String finishRegistrationTopic;
    @Value("${kafka.createDocumentsTopic}")
    private String createDocumentsTopic;
    @Value("${kafka.sendDocumentsTopic}")
    private String sendDocumentsTopic;
    @Value("${kafka.sendSesCodeTopic}")
    private String sendSesCodeTopic;
    @Value("${kafka.creditIssuedTopic}")
    private String creditIssuedTopic;
    @Value("${kafka.statementDeniedTopic}")
    private String statementDeniedTopic;
    private String finishRegistrationMessage = "Ваша заявка предварительно одобрена, завершите оформление.";
    private String createDocumentsMessage = "Ваш кредит одобрен, пожалуйста, пройдите по ссылке ниже в письме.";
    private String sendDocumentsMessage = "Пожалуйста, ознакомьтесь с документами для подписания, в письме вы также " +
            "найдете ссылку на запрос на согласие с условиями.";
    private String sendSesCodeMessage = "В данном письме вы найдете ses-код и ссылку на подписание документов. Ваш ses-код: ";
    private String creditIssuedMessage = "Вам выдан кредит, спасибо!";
    private String statementDeniedMessage = "К сожалению, вам отказано в кредите.";
    @Value("${url.request_documents}")
    private String urlRequestDocuments;
    @Value("${url.sign_documents}")
    private String urlSignDocuments;
    @Value("${url.process_ses_code}")
    private String urlProcessSesCode;
}
