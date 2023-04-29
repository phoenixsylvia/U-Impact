package com.phoenix.uimpact.configuration.mail;

public interface EmailService {

    void sendEmail(String to, String subject, String message);
}
