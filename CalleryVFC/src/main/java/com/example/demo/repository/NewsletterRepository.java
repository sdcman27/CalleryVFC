// NewsletterRepository.java
package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Newsletter;

public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {
}
