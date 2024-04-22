package com.pancake.api.setting.application;

import com.pancake.api.content.domain.Platform;
import com.pancake.api.setting.domain.Setting;
import com.pancake.api.setting.domain.SettingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SetEnablePlatform {

    private final SettingRepository repository;

    @Transactional
    public void command(Platform platform, Instant disableFrom) {
        var enablePlatform = getWith(platform);
        enablePlatform.setValue(disableFrom);
    }

    private Setting getWith(Platform platform) {
        return repository.findById(platform).orElseThrow(IllegalStateException::new);
    }
}
