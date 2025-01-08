package ru.development.Dossier.mail_service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class MailProperties {
    private String finishRegistrationSubjectLine = "Завершение регистрации";
    private String createDocumentsSubjectLine = "Ваш кредит одобрен, сформируйте документы";
    private String sendDocumentsSubjectLine = "Документы для подписания";
    private String sendSesCodeSubjectLine = "Подписание документов";
    private String creditIssuedSubjectLine = "Ваш кредит выдан";
    private String statementDeniedSubjectLine = "Отказ в кредите";
}