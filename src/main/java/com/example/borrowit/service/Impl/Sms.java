/*package com.example.borrowit.service.impl;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Sms {

    private String Sid;

    private String Token;

    private String twilioNumber;

    public Sms() {
        Twilio.init(Sid, Token);
    }

    public void sendSms(String toPhoneNumber, String messageContent) {
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(toPhoneNumber),
                new com.twilio.type.PhoneNumber(twilioNumber),
                messageContent
        ).create();

        System.out.println("Message sent: " + message.getSid());
    }
}*/
