package com.example.email_service.service;

import com.example.email_service.model.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(EmailRequest request) {
        request.getTo().forEach(recipient -> {
            try {
                MimeMessage message = emailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setTo(recipient);
                helper.setSubject(request.getSubject());
                helper.setText(request.getBody());
                if (request.getAttachments() != null) {
                    for (String attachmentPath : request.getAttachments()) {
                        File file = new File(attachmentPath);
                        if (file.exists()) {
                            helper.addAttachment(file.getName(), file);
                        }
                    }
                }
                emailSender.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
    }
}