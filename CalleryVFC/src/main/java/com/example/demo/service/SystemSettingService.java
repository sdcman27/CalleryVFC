package com.example.demo.service;

import com.example.demo.model.SystemSetting;
import com.example.demo.repository.SystemSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SystemSettingService {

    @Autowired
    private SystemSettingRepository systemSettingRepository;

    public String getSettingValue(String key) {
        Optional<SystemSetting> setting = systemSettingRepository.findBySettingKey(key);
        return setting.map(SystemSetting::getSettingValue).orElse(null);
    }

    public void updateSetting(String key, String value) {
        Optional<SystemSetting> settingOptional = systemSettingRepository.findBySettingKey(key);
        if (settingOptional.isPresent()) {
            SystemSetting setting = settingOptional.get();
            setting.setSettingValue(value);
            systemSettingRepository.save(setting);
        } else {
            SystemSetting newSetting = new SystemSetting(key, value);
            systemSettingRepository.save(newSetting);
        }
    }

    public Iterable<SystemSetting> getAllSettings() {
        return systemSettingRepository.findAll();
    }
}
