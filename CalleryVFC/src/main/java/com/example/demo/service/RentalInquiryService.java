package com.example.demo.service;

import com.example.demo.model.RentalInquiry;
import com.example.demo.repository.RentalInquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalInquiryService {

	@Autowired
    private RentalInquiryRepository rentalInquiryRepository;

    public List<RentalInquiry> getAllInquiries() {
        return rentalInquiryRepository.findAll();
    }

    public void saveInquiry(RentalInquiry rentalInquiry) {
        rentalInquiry.setStatus("PENDING");
        rentalInquiryRepository.save(rentalInquiry);
    }

    public void updateInquiryStatus(Long id, String status) {
        RentalInquiry inquiry = rentalInquiryRepository.findById(id).orElse(null);
        if (inquiry != null) {
            inquiry.setStatus(status);
            rentalInquiryRepository.save(inquiry);
        }
    }
}