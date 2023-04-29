package com.phoenix.uimpact.configuration.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class GmailEmailService implements EmailService {

private static final Logger logger = LoggerFactory.getLogger(GmailEmailService.class);
private final JavaMailSender mailSender;

@Async
@Override
public void sendEmail(String to, String subject, String message){

    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

    try{
        helper.setFrom("uimpact45@gmail.com", "UImpact");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message, true);

        mailSender.send(mimeMessage);

    } catch(MessagingException | UnsupportedEncodingException e) {
        logger.error("An error occurred while sending an email to address : "
        + to + "; error: " + e.getMessage());
    }
    logger.info(String.format("Email sent to -> %s", to));
}

}
