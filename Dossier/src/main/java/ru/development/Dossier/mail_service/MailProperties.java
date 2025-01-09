package ru.development.Dossier.mail_service;

import lombok.Getter;
import lombok.Setter;
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
    private String htmlWithPostForm = "<html>" +
            "<body>" +
            "<p>%s</p>" +
            "<form action=" + "'%s'" + " method='POST'>" +
            "<input type='submit' value='Ваша ссылка'>" +
            "</form>" +
            "</body>" +
            "</html>";

    private String htmlPostWithCode = "<html>" +
            "<body>" +
            "<p>%s</p>" +
            "<form action=" + "'%s'" + " method='POST'>" +
            "<label for='name'>Ses-код:</label><br>" +
            "<input type='text' id='name' name='code' required><br><br>" +
            "<button type='submit'>Отправить код</button>" +
            "</form>" +
            "</body>" +
            "</html>";
}