package com.example.demo.service;

import com.example.demo.model.Newsletter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendNewsletter(Newsletter newsletter, String recipientEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setFrom("sethcode12@outlook.com"); 
        message.setSubject(newsletter.getSubject());
        message.setText(newsletter.getContent());
        mailSender.send(message);
    }
    
    @Async
    public void sendTestEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("sethcode12@outlook.com");
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}
