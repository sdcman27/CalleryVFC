package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.config.PasswordEncoderConfig;
import com.example.demo.model.Announcement;
import com.example.demo.model.Equipment;
import com.example.demo.model.MaintenanceSchedule;
import com.example.demo.model.Newsletter;
import com.example.demo.model.NewsletterSubscriber;
import com.example.demo.model.SystemUsage;
import com.example.demo.model.User;
import com.example.demo.model.UserActivity;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.EquipmentRepository;
import com.example.demo.repository.MaintenanceScheduleRepository;
import com.example.demo.repository.NewsletterRepository;
import com.example.demo.repository.NewsletterSubscriberRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserActivityRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AnnouncementService;
import com.example.demo.service.BackupService;
import com.example.demo.service.EmailService;
import com.example.demo.service.SystemSettingService;
import com.example.demo.service.SystemUsageService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private static final String UPLOAD_DIR = "uploads/";
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
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
	
    @Autowired
    private SystemUsageService systemUsageService;
    
    @Autowired
    private SystemSettingService systemSettingService;

    @Autowired
    private BackupService backupService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private EquipmentRepository equipmentRepository;
    
    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private MaintenanceScheduleRepository maintenanceScheduleRepository;

    @GetMapping
    public String adminHome(Model model) {
    	model.addAttribute("announcements", announcementService.getAllAnnouncements());
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
        model.addAttribute("allRoles", roleRepository.findAll());
        return "admin/create_user";
    }

    @PostMapping("/user_list")
    public String createUser(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute User user, @RequestParam("password") String password) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        existingUser.setUsername(user.getUsername());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());

        if (password != null && !password.isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(password));
        }
        
        userRepository.save(existingUser);
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
    public String createAnnouncement(@ModelAttribute Announcement announcement, @RequestParam("imageFile") MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            try {
                Path uploadDir = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                String fileName = imageFile.getOriginalFilename();
                Path path = uploadDir.resolve(fileName);
                Files.write(path, imageFile.getBytes());
                announcement.setImageUrl("/uploads/" + fileName);  // Ensure this path is correct
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    public String updateAnnouncement(@PathVariable("id") Long id, @ModelAttribute Announcement announcement, @RequestParam("imageFile") MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            try {
                Path uploadDir = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                String fileName = imageFile.getOriginalFilename();
                Path path = uploadDir.resolve(fileName);
                Files.write(path, imageFile.getBytes());
                announcement.setImageUrl("/uploads/" + fileName);  // Ensure this path is correct
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    
    
    @GetMapping("/av-user_activity")
    public String viewUserActivity(Model model) {
        List<UserActivity> activities = userActivityRepository.findAll();
        model.addAttribute("activities", activities);
        return "admin/av-user_activity";
    }
    
    @GetMapping("/av-system_usage")
    public String systemUsage(Model model) {
        List<SystemUsage> usageStats = systemUsageService.getAllUsage();
        model.addAttribute("usageStats", usageStats);
        return "admin/av-system_usage";
    }
    
    @GetMapping("/av-settings")
    public String showSettings(Model model) {
        model.addAttribute("settings", systemSettingService.getAllSettings());
        return "admin/av-settings";
    }

    @PostMapping("/av-settings")
    public String updateSettings(@RequestParam("key") String key,
                                 @RequestParam("value") String value,
                                 Model model) {
        systemSettingService.updateSetting(key, value);
        model.addAttribute("settings", systemSettingService.getAllSettings());
        model.addAttribute("message", "Setting updated successfully");
        return "admin/av-settings";
    }

    @GetMapping("/av-backup")
    public String backup() {
        return "admin/av-backup";
    }

    @PostMapping("/av-backup")
    public String performBackup(Model model) {
        try {
            backupService.backupDatabase();
            model.addAttribute("message", "Backup created successfully, please check your downloads folder!");
        } catch (IOException e) {
            model.addAttribute("error", "Error creating backup: " + e.getMessage());
        }
        return "admin/av-backup";
    }

    @PostMapping("/av-restore")
    public String performRestore(Model model) {
        try {
            backupService.restoreDatabase();
            model.addAttribute("message", "Database restored successfully!");
        } catch (IOException e) {
            model.addAttribute("error", "Error restoring database: " + e.getMessage());
        }
        return "admin/av-backup";
    }

    @GetMapping("/av-equipment")
    public String listEquipment(Model model) {
        List<Equipment> equipmentList = equipmentRepository.findAll();
        model.addAttribute("equipmentList", equipmentList);
        return "admin/av-equipment";
    }

    @GetMapping("/av-equipment/new")
    public String showCreateEquipmentForm(Model model) {
        model.addAttribute("equipment", new Equipment());
        return "admin/av-create_equipment";
    }

    @PostMapping("/av-equipment")
    public String createEquipment(@ModelAttribute Equipment equipment) {
        equipmentRepository.save(equipment);
        return "redirect:/admin/av-equipment";
    }

    @GetMapping("/av-equipment/edit/{id}")
    public String showEditEquipmentForm(@PathVariable("id") Long id, Model model) {
        Equipment equipment = equipmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid equipment Id:" + id));
        model.addAttribute("equipment", equipment);
        return "admin/av-edit_equipment";
    }

    @PostMapping("/av-equipment/update/{id}")
    public String updateEquipment(@PathVariable("id") Long id, @ModelAttribute Equipment equipment) {
        equipmentRepository.save(equipment);
        return "redirect:/admin/av-equipment";
    }

    @GetMapping("/av-equipment/delete/{id}")
    public String deleteEquipment(@PathVariable("id") Long id) {
        Equipment equipment = equipmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid equipment Id:" + id));
        equipmentRepository.delete(equipment);
        return "redirect:/admin/av-equipment";
    }

    @GetMapping("/av-maintenance")
    public String listMaintenanceSchedules(Model model) {
        List<MaintenanceSchedule> maintenanceList = maintenanceScheduleRepository.findAll();
        model.addAttribute("maintenanceList", maintenanceList);
        return "admin/av-maintenance";
    }

    @GetMapping("/av-maintenance/new")
    public String showCreateMaintenanceForm(Model model) {
        model.addAttribute("maintenanceSchedule", new MaintenanceSchedule());
        return "admin/av-create_maintenance";
    }

    @PostMapping("/av-maintenance")
    public String createMaintenance(@ModelAttribute MaintenanceSchedule maintenanceSchedule) {
        maintenanceScheduleRepository.save(maintenanceSchedule);
        return "redirect:/admin/av-maintenance";
    }

    @GetMapping("/av-maintenance/edit/{id}")
    public String showEditMaintenanceForm(@PathVariable("id") Long id, Model model) {
        MaintenanceSchedule maintenanceSchedule = maintenanceScheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid maintenance Id:" + id));
        model.addAttribute("maintenanceSchedule", maintenanceSchedule);
        return "admin/av-edit_maintenance";
    }

    @PostMapping("/av-maintenance/update/{id}")
    public String updateMaintenance(@PathVariable("id") Long id, @ModelAttribute MaintenanceSchedule maintenanceSchedule) {
        maintenanceScheduleRepository.save(maintenanceSchedule);
        return "redirect:/admin/av-maintenance";
    }

    @GetMapping("/av-maintenance/delete/{id}")
    public String deleteMaintenance(@PathVariable("id") Long id) {
        MaintenanceSchedule maintenanceSchedule = maintenanceScheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid maintenance Id:" + id));
        maintenanceScheduleRepository.delete(maintenanceSchedule);
        return "redirect:/admin/av-maintenance";
    }

}


