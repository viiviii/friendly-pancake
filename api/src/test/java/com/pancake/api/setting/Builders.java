package com.pancake.api.setting;

import com.pancake.api.content.domain.Platform;
import com.pancake.api.setting.api.SettingApiController.PlatformSettingResponse;
import com.pancake.api.setting.domain.Setting;
import lombok.Builder;

import java.time.Instant;

import static com.pancake.api.content.domain.Platform.DISNEY_PLUS;
import static com.pancake.api.content.domain.Platform.NETFLIX;
import static com.pancake.api.setting.api.SettingApiController.EnableRequest;

public abstract class Builders {

    public static Builders.EnableRequestBuilder aEnableSetting() {
        return enableSettingBuilder();
    }

    public static Builders.PlatformSettingResponseBuilder aPlatformSettingResponse() {
        return platformSettingResponseBuilder().platform(NETFLIX);
    }

    public static Builders.SettingBuilder aSetting() {
        return settingBuilder().platform(DISNEY_PLUS);
    }

    @Builder(builderMethodName = "enableSettingBuilder")
    private static EnableRequest create(String disableFrom) {
        return new EnableRequest(disableFrom(disableFrom));
    }

    @Builder(builderMethodName = "platformSettingResponseBuilder")
    private static PlatformSettingResponse platformSettingResponse(Platform platform, String disableFrom) {
        return new PlatformSettingResponse(platform.label(), platform.name(), disableFrom(disableFrom));
    }

    @Builder(builderMethodName = "settingBuilder")
    private static Setting setting(Platform platform, String disableFrom) {
        return new Setting(platform, disableFrom(disableFrom));
    }

    private static Instant disableFrom(String value) {
        if (value == null) {
            return null;
        }
        return Instant.parse(value);
    }
}