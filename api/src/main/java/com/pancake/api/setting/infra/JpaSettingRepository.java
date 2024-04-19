package com.pancake.api.setting.infra;

import com.pancake.api.content.domain.Platform;
import com.pancake.api.setting.domain.Setting;
import com.pancake.api.setting.domain.SettingRepository;
import com.pancake.api.watch.domain.FindEnabledPlatforms;
import org.springframework.data.repository.Repository;

import java.util.List;

interface JpaSettingRepository extends Repository<Setting, Platform>,
        SettingRepository, FindEnabledPlatforms {

    @Override
    default List<Platform> findEnabledPlatforms() {
        return findAll().stream()
                .filter(Setting::isEnabled)
                .map(Setting::getPlatform)
                .toList();
    }

    List<Setting> findAll();
}
