package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserActivity;
import com.example.demo.repository.UserActivityRepository;

@Service
public class UserActivityService {

    @Autowired
    private UserActivityRepository userActivityRepository;

    private Set<String> activeUsers = new HashSet<>();

    public void logActivity(String username, String action) {
        UserActivity activity = new UserActivity();
        activity.setUsername(username);
        activity.setAction(action);
        activity.setTimestamp(LocalDateTime.now());
        userActivityRepository.save(activity);

        if ("Successful login".equals(action)) {
            activeUsers.add(username);
        } else if ("Logout".equals(action)) {
            activeUsers.remove(username);
        }
    }

    public Set<String> getActiveUsers() {
        return activeUsers;
    }
}