package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class MaintenanceSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String equipmentName;
    private LocalDate maintenanceDate;
    private String details;
	
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	public LocalDate getMaintenanceDate() {
		return maintenanceDate;
	}
	public void setMaintenanceDate(LocalDate maintenanceDate) {
		this.maintenanceDate = maintenanceDate;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}

    
}
