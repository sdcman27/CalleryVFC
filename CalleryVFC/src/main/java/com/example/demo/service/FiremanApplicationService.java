package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.FiremanApplication;
import com.example.demo.repository.FiremanApplicationRepository;

import java.util.List;

@Service
public class FiremanApplicationService {

    @Autowired
    private FiremanApplicationRepository repository;

    public void saveApplication(FiremanApplication applications) {
        repository.save(applications);
    }

    public List<FiremanApplication> getAllApplications() {
        return repository.findAll();
    }
}

