package ru.development.Dossier.mail_service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
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
        javaMailSender.send(message);
    }
}
