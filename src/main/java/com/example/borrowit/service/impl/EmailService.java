package com.example.borrowit.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
    public void sendPaymentConfirmation(String toEmail, String clientName, Long contractId,
                                        byte[] contractPdf, byte[] invoicePdf) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("Confirmation de paiement - Contrat N° " + contractId);

        String emailContent = "Bonjour " + clientName + ",\n\n" +
                "Votre paiement pour le contrat N° " + contractId + " a bien été reçu.\n" +
                "Vous trouverez ci-joint votre contrat et votre facture.\n\n" +
                "Cordialement,\nL'équipe de service";

        helper.setText(emailContent);

        helper.addAttachment("Contrat_" + contractId + ".pdf", new ByteArrayResource(contractPdf));
        helper.addAttachment("Facture_" + contractId + ".pdf", new ByteArrayResource(invoicePdf));

        mailSender.send(message);
    }

}