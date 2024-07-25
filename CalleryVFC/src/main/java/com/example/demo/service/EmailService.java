package com.example.demo.service;

import com.example.demo.model.Newsletter;
import com.example.demo.repository.NewsletterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private NewsletterRepository newsletterRepository;

    @Async
    public void sendNewsletter(Newsletter newsletter, String recipientEmail) {
    	try {
    	SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setFrom("sethcode12@outlook.com"); 
        message.setSubject(newsletter.getSubject());
        message.setText(newsletter.getContent());
        mailSender.send(message);
        newsletter.setSent(true);
        newsletterRepository.save(newsletter);
    	} catch (Exception e) {
        e.printStackTrace();
    }
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
