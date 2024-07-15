package com.example.demo.repository;

import com.example.demo.model.RentalInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalInquiryRepository extends JpaRepository<RentalInquiry, Long> {
}
