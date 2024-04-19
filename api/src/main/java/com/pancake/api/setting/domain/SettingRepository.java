package com.pancake.api.setting.domain;

import com.pancake.api.content.domain.Platform;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface SettingRepository extends Repository<Setting, Platform> {

    Optional<Setting> findById(Platform platform);
}
