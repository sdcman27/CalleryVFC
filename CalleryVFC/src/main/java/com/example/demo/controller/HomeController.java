package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.NewsletterSubscriber;
import com.example.demo.repository.NewsletterSubscriberRepository;

@Controller
public class HomeController {
	
	@Autowired
	private NewsletterSubscriberRepository subscriberRepository;

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }
	
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/subscribe")
    public String showSubscriptionForm() {
        return "subscribe";
    }
    
    @PostMapping("/subscribe")
    public String subscribe(@RequestParam("email") String email, Model model) {
        NewsletterSubscriber subscriber = new NewsletterSubscriber();
        subscriber.setEmail(email);
        subscriberRepository.save(subscriber);
        model.addAttribute("message", "Subscribed successfully!");
        return "subscribe";
    }
}