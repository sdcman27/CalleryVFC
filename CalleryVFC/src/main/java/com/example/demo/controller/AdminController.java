package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Announcement;
import com.example.demo.model.Newsletter;
import com.example.demo.model.NewsletterSubscriber;
import com.example.demo.model.User;
import com.example.demo.model.UserActivity;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.NewsletterRepository;
import com.example.demo.repository.NewsletterSubscriberRepository;
import com.example.demo.repository.UserActivityRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AnnouncementRepository announcementRepository;
	
	@Autowired
    private NewsletterRepository newsletterRepository;
	
	@Autowired
    private EmailService emailService;
	
	@Autowired
    private NewsletterSubscriberRepository subscriberRepository;
	
	@Autowired
    private UserActivityRepository userActivityRepository;

    @GetMapping
    public String adminHome() {
        return "admin/admin";
    }
    
    @GetMapping("/user_list")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/user_list";
    }

    @GetMapping("/user_list/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/create_user";
    }

    @PostMapping("/user_list")
    public String createUser(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/admin/user_list";
    }

    @GetMapping("/user_list/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "admin/edit_user";
    }

    @PostMapping("/user_list/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/admin/user_list";
    }

    @GetMapping("/user_list/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        return "redirect:/admin/user_list";
    }
    
 // Announcements
    @GetMapping("/av-announcements")
    public String listAnnouncements(Model model) {
        List<Announcement> announcements = announcementRepository.findAll();
        model.addAttribute("announcements", announcements);
        return "admin/av-announcements";
    }

    @GetMapping("/av-announcements/new")
    public String showCreateAnnouncementForm(Model model) {
        model.addAttribute("announcement", new Announcement());
        return "admin/av-create_announcement";
    }

    @PostMapping("/av-announcements")
    public String createAnnouncement(@ModelAttribute Announcement announcement) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());
        announcement.setUser(user);
        announcementRepository.save(announcement);
        return "redirect:/admin/av-announcements";
    }

    @GetMapping("/av-announcements/edit/{id}")
    public String showEditAnnouncementForm(@PathVariable("id") Long id, Model model) {
        Announcement announcement = announcementRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid announcement Id:" + id));
        model.addAttribute("announcement", announcement);
        return "admin/av-edit_announcement";
    }

    @PostMapping("/av-announcements/update/{id}")
    public String updateAnnouncement(@PathVariable("id") Long id, @ModelAttribute Announcement announcement) {
        announcementRepository.save(announcement);
        return "redirect:/admin/av-announcements";
    }

    @GetMapping("/av-announcements/delete/{id}")
    public String deleteAnnouncement(@PathVariable("id") Long id) {
        Announcement announcement = announcementRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid announcement Id:" + id));
        announcementRepository.delete(announcement);
        return "redirect:/admin/av-announcements";
    }
    
 // Newsletters
    @GetMapping("/av-newsletters")
    public String listNewsletters(Model model) {
        List<Newsletter> newsletters = newsletterRepository.findAll();
        model.addAttribute("newsletters", newsletters);
        return "admin/av-newsletters";
    }

    @GetMapping("/av-newsletters/new")
    public String showCreateNewsletterForm(Model model) {
        model.addAttribute("newsletter", new Newsletter());
        return "admin/av-create_newsletter";
    }

    @PostMapping("/av-newsletters")
    public String createNewsletter(@ModelAttribute Newsletter newsletter) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());
        newsletter.setUser(user);
        newsletterRepository.save(newsletter);
        return "redirect:/admin/av-newsletters";
    }

    @GetMapping("/av-newsletters/edit/{id}")
    public String showEditNewsletterForm(@PathVariable("id") Long id, Model model) {
        Newsletter newsletter = newsletterRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid newsletter Id:" + id));
        model.addAttribute("newsletter", newsletter);
        return "admin/av-edit_newsletter";
    }

    @PostMapping("/av-newsletters/update/{id}")
    public String updateNewsletter(@PathVariable("id") Long id, @ModelAttribute Newsletter newsletter) {
        newsletterRepository.save(newsletter);
        return "redirect:/admin/av-newsletters";
    }

    @GetMapping("/av-newsletters/delete/{id}")
    public String deleteNewsletter(@PathVariable("id") Long id) {
        Newsletter newsletter = newsletterRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid newsletter Id:" + id));
        newsletterRepository.delete(newsletter);
        return "redirect:/admin/av-newsletters";
    }
    
    @GetMapping("/av-send_test_email")
    public String showTestEmailForm() {
        return "admin/av-send_test_email";
    }

    @PostMapping("/av-send_test_email")
    public String sendTestEmail(@RequestParam(value = "to", required = false) String to,
            					@RequestParam("subject") String subject,
            					@RequestParam("content") String content,
            					@RequestParam("schedule") String schedule,
            					@RequestParam(value = "sendDateTime", required = false) String sendDateTime,
            					@RequestParam(value = "includeSubscribers", required = false) Boolean includeSubscribers,
            					Model model) {
  
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());

        Newsletter newsletter = new Newsletter();
        newsletter.setUser(user);
        newsletter.setSubject(subject);
        newsletter.setContent(content);
        List<String> recipients = new ArrayList<>();

        if (Boolean.TRUE.equals(includeSubscribers)) {
            List<NewsletterSubscriber> subscribers = subscriberRepository.findAll();
            for (NewsletterSubscriber subscriber : subscribers) {
                recipients.add(subscriber.getEmail());
            }
        } else if (to != null && !to.isEmpty()) {
            recipients.add(to);
        } else {
            model.addAttribute("message", "Please specify a recipient email or select 'Include Subscribers'.");
            return "admin/av-send_test_email";
        }

        if ("now".equals(schedule)) {
            newsletter.setSendDateTime(LocalDateTime.now());
            for (String recipient : recipients) {
                emailService.sendTestEmail(recipient, subject, content);
            }
            model.addAttribute("message", "Test email sent successfully and saved to the newsletter repository!");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime scheduledDateTime = LocalDateTime.parse(sendDateTime, formatter);
            newsletter.setSendDateTime(scheduledDateTime);
            newsletterRepository.save(newsletter);
            model.addAttribute("message", "Test email scheduled successfully and saved to the newsletter repository!");
        }

        return "admin/av-send_test_email";
    }
    
    @GetMapping("/admin/user-activity")
    public String viewUserActivity(Model model) {
        List<UserActivity> activities = userActivityRepository.findAll();
        model.addAttribute("activities", activities);
        return "admin/user-activity";
    }

}


