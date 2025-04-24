package com.example.borrowit.service.Impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${frontend.url}") // Utilisez directement l'URL du frontend
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        // Construction de l'URL frontend
        String resetLink = frontendUrl + "/reset-password?token=" + resetToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Réinitialisation de mot de passe");
        message.setText(String.format(
                "Pour réinitialiser votre mot de passe, cliquez sur le lien suivant : %s\n\n" +
                        "Ce lien expirera dans 30 minutes.\n\n" +
                        "Si vous n'avez pas demandé de réinitialisation, ignorez cet email.",
                resetLink
        ));

        mailSender.send(message);
    }
}