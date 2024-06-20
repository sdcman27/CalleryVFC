package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.UserActivity;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

	List<UserActivity> findAll();
}

