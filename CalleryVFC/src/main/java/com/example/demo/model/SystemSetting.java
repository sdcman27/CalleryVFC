package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class SystemSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String settingKey;
    private String settingValue;

    public SystemSetting() {
        // Default constructor required by JPA
    }

    public SystemSetting(String key, String value) {
        this.settingKey = key;
        this.settingValue = value;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }
}
