package com.pancake.api.setting.application;

import com.pancake.api.content.domain.Platform;
import com.pancake.api.setting.domain.DisableDateTime;
import com.pancake.api.setting.domain.Setting;
import com.pancake.api.setting.domain.SettingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SetEnablePlatform {

    private final SettingRepository repository;

    @Transactional
    public void command(Platform platform, DisableDateTime disableFrom) {
        var setting = settingWith(platform);
        setting.disableFrom(disableFrom);
    }

    private Setting settingWith(Platform platform) {
        return repository.findById(platform)
                .orElseThrow(IllegalStateException::new);
    }
}
