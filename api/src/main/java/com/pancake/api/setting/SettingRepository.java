package com.pancake.api.setting;

import com.pancake.api.content.domain.Platform;
import org.springframework.data.repository.Repository;

import java.util.Optional;

interface SettingRepository extends Repository<Setting, Platform> {

    Optional<Setting> findById(Platform platform);
}
