package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class BackupService {

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    private static final String BACKUP_FILE = "backup.sql";

    public void backupDatabase() throws IOException {
        String databaseName = extractDatabaseName(dbUrl);
        String command = String.format("mysqldump -u %s -p%s %s > %s", dbUsername, dbPassword, databaseName, BACKUP_FILE);
        executeCommand(command);
    }

    public void restoreDatabase() throws IOException {
        String databaseName = extractDatabaseName(dbUrl);
        String command = String.format("mysql -u %s -p%s %s < %s", dbUsername, dbPassword, databaseName, BACKUP_FILE);
        executeCommand(command);
    }

    private void executeCommand(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    private String extractDatabaseName(String url) {
        String[] parts = url.split("/");
        return parts[parts.length - 1].split("\\?")[0];
    }
}
