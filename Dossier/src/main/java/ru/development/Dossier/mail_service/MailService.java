package ru.development.Dossier.mail_service;

import jakarta.mail.MessagingException;

public interface MailService {
    void send(String address, String body, String subject);
    void sendHtml(String address, String body, String subject) throws MessagingException;


}
