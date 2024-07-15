package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.FiremanApplication;

public interface FiremanApplicationRepository extends JpaRepository<FiremanApplication, Long> {
}
