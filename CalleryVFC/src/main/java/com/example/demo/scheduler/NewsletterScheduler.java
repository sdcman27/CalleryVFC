// NewsletterScheduler.java
package com.example.demo.scheduler;

import com.example.demo.model.Newsletter;
import com.example.demo.repository.NewsletterRepository;
import com.example.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Component
public class NewsletterScheduler {

    @Autowired
    private NewsletterRepository newsletterRepository;

    @Autowired
    private EmailService emailService;
    
    private static final Logger logger = Logger.getLogger(NewsletterScheduler.class.getName());

    @Scheduled(fixedRate = 60000) // Run every minute
    public void sendScheduledNewsletters() {
        logger.info("Running scheduled task to send newsletters.");
        List<Newsletter> newsletters = newsletterRepository.findAll();
        for (Newsletter newsletter : newsletters) {
            logger.info("Checking newsletter with ID: " + newsletter.getId() + " scheduled for: " + newsletter.getSendDateTime());
            if (newsletter.getSendDateTime().isBefore(LocalDateTime.now())) {
                logger.info("Sending newsletter with ID: " + newsletter.getId());
                // Fetch all subscribers (you need to implement fetching subscribers)
                List<String> subscribers = getSubscribers();
                for (String email : subscribers) {
                    emailService.sendNewsletter(newsletter, email);
                }
                newsletterRepository.delete(newsletter); // Remove the newsletter after sending
                logger.info("Deleted sent newsletter with ID: " + newsletter.getId());
            }
        }
    }


    private List<String> getSubscribers() {
        // Implement this to fetch the list of subscriber emails
        // For now, return an example list
        return List.of("subscriber@example.com");
    }
}
