// NewsletterScheduler.java
package com.example.demo.scheduler;

import com.example.demo.model.Newsletter;
import com.example.demo.model.NewsletterSubscriber;
import com.example.demo.repository.NewsletterRepository;
import com.example.demo.repository.NewsletterSubscriberRepository;
import com.example.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class NewsletterScheduler {

    @Autowired
    private NewsletterRepository newsletterRepository;
    
    @Autowired
    private NewsletterSubscriberRepository newsletterSubscriberRepository;

    @Autowired
    private EmailService emailService;
    
    private static final Logger logger = Logger.getLogger(NewsletterScheduler.class.getName());

    @Scheduled(fixedRate = 60000) // Run every minute
    public void sendScheduledNewsletters() {
        logger.info("Running scheduled task to scan and send timed newsletters.");
        List<Newsletter> newsletters = newsletterRepository.findAll();
        for (Newsletter newsletter : newsletters) {
            logger.info("Checking newsletter with ID: " + newsletter.getId() + " scheduled for: " + newsletter.getSendDateTime());
            if (newsletter.getSendDateTime().isBefore(LocalDateTime.now())) {
                logger.info("Sending newsletter with ID: " + newsletter.getId());
                List<String> subscribers = getSubscribers();
                for (String email : subscribers) {
                    try {
                        emailService.sendNewsletter(newsletter, email);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.severe("Failed to send email to: " + email);
                    }
                }
                newsletter.setSent(true);
                newsletterRepository.save(newsletter);
                logger.info("Marked newsletter with ID: " + newsletter.getId() + " as sent.");
            }
        }
    }


    private List<String> getSubscribers() {
    /*	
    	List<NewsletterSubscriber> subscribers = newsletterSubscriberRepository.findAll();
        return subscribers.stream()
                          .map(NewsletterSubscriber::getEmail)
                          .collect(Collectors.toList());
    }
    
    */
        return List.of("sethchritzman@gmail.com");
    }
}
