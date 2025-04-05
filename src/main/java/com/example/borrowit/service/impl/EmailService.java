package com.example.borrowit.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.reset.path}")
    private String resetPath;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${server.port}")
    private String serverPort;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        String resetLink = "http://localhost:" + serverPort + contextPath + resetPath + "?token=" + resetToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Réinitialisation de mot de passe");
        message.setText(String.format(
                "Pour réinitialiser votre mot de passe, cliquez sur le lien suivant : %s\n\n" +
                        "Ce lien expirera dans 30 minutes.",
                resetLink
        ));

        mailSender.send(message);
    }
}
