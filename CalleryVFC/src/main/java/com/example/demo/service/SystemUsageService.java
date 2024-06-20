package com.example.demo.service;

import com.example.demo.model.SystemUsage;
import com.example.demo.repository.SystemUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SystemUsageService {

    @Autowired
    private SystemUsageRepository systemUsageRepository;

    public void logUsage(String metric, Long value) {
        SystemUsage usage = new SystemUsage();
        usage.setMetric(metric);
        usage.setValue(value);
        usage.setTimestamp(LocalDateTime.now());
        systemUsageRepository.save(usage);
    }

    public List<SystemUsage> getAllUsage() {
        return systemUsageRepository.findAll();
    }
}
