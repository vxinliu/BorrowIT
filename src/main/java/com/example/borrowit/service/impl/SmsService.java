package com.example.borrowit.service.impl;

import com.twilio.Twilio;
import com.twilio.exception.AuthenticationException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    @PostConstruct
    public void init() {
        try {
            Twilio.init(accountSid, authToken);
            logger.info("✅ Twilio initialisé avec succès (SID: {})", accountSid);
        } catch (AuthenticationException e) {
            logger.error("❌ Échec de l'initialisation de Twilio", e);
            throw new RuntimeException("Échec de l'initialisation de Twilio", e);
        }
    }

    public void sendSms(String toPhoneNumber, String messageBody) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(fromPhoneNumber),
                    messageBody
            ).create();

            logger.info("📨 SMS envoyé à {} (SID: {})", toPhoneNumber, message.getSid());
        } catch (Exception e) {
            logger.error("❌ Échec de l'envoi du SMS à {}", toPhoneNumber, e);
            throw new RuntimeException("Échec de l'envoi du SMS", e);
        }
    }

    public void sendVerificationCode(String toPhoneNumber, String verificationCode) {
        String message = String.format("Votre code de vérification BorrowIT : %s", verificationCode);
        sendSms(toPhoneNumber, message);
    }
}
