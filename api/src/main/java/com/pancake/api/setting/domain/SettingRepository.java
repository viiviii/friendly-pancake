package com.pancake.api.setting.domain;

import com.pancake.api.content.domain.Platform;

import java.util.List;
import java.util.Optional;

public interface SettingRepository {

    Optional<Setting> findById(Platform platform);

    List<Setting> findAll();
}
