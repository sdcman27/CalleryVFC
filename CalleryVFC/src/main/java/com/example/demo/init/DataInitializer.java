package com.example.demo.init;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import jakarta.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            createDatabaseIfNotExist();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating database", e);
        }

        if (roleRepository.findAll().isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);

            Role moderatorRole = new Role();
            moderatorRole.setName("ROLE_MODERATOR");
            roleRepository.save(moderatorRole);

            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            admin.setRoles(adminRoles);
            userRepository.save(admin);

            User moderator = new User();
            moderator.setUsername("moderator");
            moderator.setPassword(passwordEncoder.encode("moderator"));
            Set<Role> moderatorRoles = new HashSet<>();
            moderatorRoles.add(moderatorRole);
            moderator.setRoles(moderatorRoles);
            userRepository.save(moderator);
        }
    }

    private void createDatabaseIfNotExist() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE DATABASE IF NOT EXISTS fire_department_db");
            statement.execute("USE fire_department_db");
        }
    }
}