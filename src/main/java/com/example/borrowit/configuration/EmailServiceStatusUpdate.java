package com.example.borrowit.configuration;


import com.example.borrowit.Entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
@Service
public class EmailServiceStatusUpdate {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendStatusUpdateEmail(String to, Item item) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Mise à jour du statut de votre article");

            String content = "<p>Bonjour,</p>" +
                    "<p>Le statut de votre article <strong>" + item.getName() + "</strong> a été mis à jour.</p>" +
                    "<p>Nouveau statut : <strong>" + item.getStatusItem() + "</strong></p>" +
                    "<p>Merci de votre confiance.</p>";

            helper.setText(content, true);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            System.out.println("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
        }
    }
}
