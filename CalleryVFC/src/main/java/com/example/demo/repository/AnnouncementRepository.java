// AnnouncementRepository.java
package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
}
