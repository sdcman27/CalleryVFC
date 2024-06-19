package com.example.demo.repository;

import com.example.demo.model.NewsletterSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsletterSubscriberRepository extends JpaRepository<NewsletterSubscriber, Long> {
}
