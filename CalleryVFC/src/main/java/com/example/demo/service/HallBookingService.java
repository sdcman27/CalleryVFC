package com.example.demo.service;

import com.example.demo.model.HallBooking;
import com.example.demo.repository.HallBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HallBookingService {

    @Autowired
    private HallBookingRepository hallBookingRepository;

    public List<HallBooking> getAllBookings() {
        return hallBookingRepository.findAll();
    }

    public void saveBooking(HallBooking hallBooking) {
        hallBookingRepository.save(hallBooking);
    }
}