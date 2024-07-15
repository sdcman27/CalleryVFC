package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.AnnouncementService;
import com.example.demo.service.FiremanApplicationService;
import com.example.demo.service.RentalInquiryService;
import com.example.demo.model.Announcement;
import com.example.demo.model.FiremanApplication;
import com.example.demo.model.RentalInquiry;

@Controller
@RequestMapping("/moderator")
public class ModeratorController {


    @Autowired
    private RentalInquiryService rentalInquiryService;
    
    @Autowired
    private AnnouncementService announcementService;
    
    @Autowired
    private FiremanApplicationService firemanApplicationService;
	
    @GetMapping
    public String moderatorHome() {
        return "moderator/moderator";
    }

    @GetMapping("/inquiries")
    public String viewInquiries(Model model) {
        List<RentalInquiry> inquiries = rentalInquiryService.getAllInquiries();
        model.addAttribute("inquiries", inquiries);
        return "moderator/mv-inquiries";
    }

    @PostMapping("/inquiry/accept")
    public String acceptInquiry(@RequestParam("id") Long id) {
        rentalInquiryService.updateInquiryStatus(id, "ACCEPTED");
        return "redirect:/moderator/inquiries";
    }

    @PostMapping("/inquiry/decline")
    public String declineInquiry(@RequestParam("id") Long id) {
        rentalInquiryService.updateInquiryStatus(id, "DECLINED");
        return "redirect:/moderator/inquiries";
    }
    
    @GetMapping("/announcements")
    public String viewAnnouncements(Model model) {
        List<Announcement> announcements = announcementService.getAllAnnouncements();
        model.addAttribute("announcements", announcements);
        return "moderator/mv-announcements";
    }

    @GetMapping("/announcement/new")
    public String newAnnouncementForm(Model model) {
        model.addAttribute("announcement", new Announcement());
        return "moderator/mv-create_announcement";
    }

    @PostMapping("/announcement")
    public String saveAnnouncement(@ModelAttribute Announcement announcement) {
        announcementService.saveAnnouncement(announcement);
        return "redirect:/moderator/announcements";
    }

    @GetMapping("/announcement/edit/{id}")
    public String editAnnouncementForm(@PathVariable Long id, Model model) {
        Announcement announcement = announcementService.getAnnouncementById(id);
        model.addAttribute("announcement", announcement);
        return "moderator/mv-edit_announcement";
    }

    @PostMapping("/announcement/update/{id}")
    public String updateAnnouncement(@PathVariable Long id, @ModelAttribute Announcement announcement) {
        announcement.setId(id);
        announcementService.saveAnnouncement(announcement);
        return "redirect:/moderator/announcements";
    }

    @PostMapping("/announcement/delete/{id}")
    public String deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return "redirect:/moderator/announcements";
    }
    
    @GetMapping("/applications")
    public String viewApplications(Model model) {
        List<FiremanApplication> applications = firemanApplicationService.getAllApplications();
        model.addAttribute("applications", applications);
        return "moderator/mv-applications";
    }
}
