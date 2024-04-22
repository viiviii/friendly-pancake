package com.pancake.api.setting;

import lombok.Builder;

import java.time.Instant;

import static com.pancake.api.setting.api.SettingApiController.EnableRequest;

public abstract class Builders {

    public static Builders.EnableRequestBuilder aEnabledSetting() {
        return enableSettingBuilder();
    }

    @Builder(builderMethodName = "enableSettingBuilder")
    private static EnableRequest create(String disabledAt) {
        return new EnableRequest(Instant.parse(disabledAt));
    }
}