package com.example.demo.repository;

import com.example.demo.model.SystemUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemUsageRepository extends JpaRepository<SystemUsage, Long> {
}
