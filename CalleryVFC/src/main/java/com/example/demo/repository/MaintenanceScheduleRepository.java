package com.example.demo.repository;

import com.example.demo.model.MaintenanceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceScheduleRepository extends JpaRepository<MaintenanceSchedule, Long> {
}
