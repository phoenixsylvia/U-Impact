package com.phoenix.uimpact.configuration.mail;

public interface MailService {

    void sendEmail(String to, String subject, String message);
}
