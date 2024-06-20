package com.example.demo.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.example.demo.service.SystemUsageService;
import com.example.demo.service.UserActivityService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserActivityService userActivityService;
    
    @Autowired
    private SystemUsageService systemUsageService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");

        // Log failed login attempt
        userActivityService.logActivity(username, "Failed login attempt");
        
     // Increment failed login attempts
        systemUsageService.logUsage("failed_login_attempts", 1L);

        super.onAuthenticationFailure(request, response, exception);
    }
}
