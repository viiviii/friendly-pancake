package com.pancake.api.setting.application;

import com.pancake.api.setting.domain.Setting;
import com.pancake.api.setting.domain.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPlatformSettings {

    private final SettingRepository repository;

    public List<Setting> query() {
        return repository.findAll();
    }
}
