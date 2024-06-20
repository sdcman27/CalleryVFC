package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.service.UserActivityService;
import com.example.demo.service.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailServiceImpl userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;
    private final UserActivityService userActivityService;


    @Autowired
    public SecurityConfig(UserDetailServiceImpl userDetailsService, BCryptPasswordEncoder passwordEncoder, CustomAuthenticationSuccessHandler successHandler, CustomAuthenticationFailureHandler failureHandler, UserActivityService userActivityService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.userActivityService = userActivityService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/home", "/login", "/logout", "/subscribe", "/calendar").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/moderator/**").hasAuthority("ROLE_MODERATOR")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home") // Ensure this is correctly set to /home
                .addLogoutHandler((request, response, authentication) -> {
                    if (authentication != null) {
                        userActivityService.logActivity(authentication.getName(), "Logout");
                    }
                })
                .permitAll()
            );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}