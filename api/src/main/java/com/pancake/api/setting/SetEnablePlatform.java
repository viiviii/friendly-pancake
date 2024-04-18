package com.pancake.api.setting;

import com.pancake.api.content.domain.Platform;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SetEnablePlatform {

    private final SettingRepository repository;

    @Transactional
    public void command(Platform platform, LocalDate disableAt) {
        var setting = settingWith(platform);
        setting.disableFrom(disableAt);
    }

    private Setting settingWith(Platform platform) {
        return repository.findById(platform)
                .orElseThrow(IllegalStateException::new);
    }
}
