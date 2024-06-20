package com.example.demo.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.service.SystemUsageService;
import com.example.demo.service.UserActivityService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	@Autowired
	private UserActivityService userActivityService;
	
    @Autowired
    private SystemUsageService systemUsageService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String targetUrl = determineTargetUrl(authorities);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
        userActivityService.logActivity(authentication.getName(), "Successful login");
        systemUsageService.logUsage("login_attempts", 1L);


    }

    private String determineTargetUrl(Collection<? extends GrantedAuthority> authorities) {
        for (GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if (authorityName.equals("ROLE_ADMIN")) {
                return "/admin";
            } else if (authorityName.equals("ROLE_MODERATOR")) {
                return "/moderator";
            } else {
                return "/home";
            }
        }
        throw new IllegalStateException("User has no appropriate role assigned.");
    }
}