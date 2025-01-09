package ru.development.Dossier.mail_service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.development.Dossier.mail_service.interfaces.MailService;

@RequiredArgsConstructor
@Service
@Slf4j
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String addressFrom;

    @Override
    public void send(String address, String body, String subject) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(addressFrom);
        simpleMailMessage.setTo(address);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        log.debug("Отправляется текстовое сообщение по адресу: {}, тема письма: {}", address, subject);
        javaMailSender.send(simpleMailMessage);
    }

    @Override
    public void sendHtml(String address, String body, String subject) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);  // true для поддержки вложений и HTML

        helper.setFrom(addressFrom);
        helper.setTo(address);
        helper.setSubject(subject);
        helper.setText(body, true);
        log.debug("Отправляется сообщение с html содержимым по адресу: {}, тема письма: {}", address, subject);
        javaMailSender.send(message);
    }
}
