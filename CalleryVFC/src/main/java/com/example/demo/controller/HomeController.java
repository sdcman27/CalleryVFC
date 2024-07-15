package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Announcement;
import com.example.demo.model.FiremanApplication;
import com.example.demo.model.HallBooking;
import com.example.demo.model.NewsletterSubscriber;
import com.example.demo.model.RentalInquiry;
import com.example.demo.repository.NewsletterSubscriberRepository;
import com.example.demo.service.AnnouncementService;
import com.example.demo.service.FiremanApplicationService;
import com.example.demo.service.HallBookingService;
import com.example.demo.service.RentalInquiryService;

@Controller
public class HomeController {
	
	@Autowired
	private NewsletterSubscriberRepository subscriberRepository;

	@Autowired
	private HallBookingService hallBookingService;

	@Autowired
	private RentalInquiryService rentalInquiryService;
	
	@Autowired
    private AnnouncementService announcementService;
	
	@Autowired
    private FiremanApplicationService firemanApplicationService;
	    
    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }
	
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("announcements", announcementService.getAllAnnouncements());
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
    
    @GetMapping("/calendar")
    public String viewCalendar(Model model) {
        List<HallBooking> bookings = hallBookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        model.addAttribute("rentalInquiry", new RentalInquiry());
        return "calendar";
    }

    @PostMapping("/inquiry")
    public String submitInquiry(@ModelAttribute RentalInquiry rentalInquiry, Model model) {
        rentalInquiryService.saveInquiry(rentalInquiry);
        model.addAttribute("message", "Inquiry submitted successfully!");
        return "redirect:/calendar";
    }
    
    @GetMapping("/apply")
    public String showApplicationForm(Model model) {
        model.addAttribute("application", new FiremanApplication());
        return "apply";
    }

    @PostMapping("/apply")
    public String submitApplication(@ModelAttribute FiremanApplication application) {
    	firemanApplicationService.saveApplication(application);
        return "redirect:/apply?success";
    }
}