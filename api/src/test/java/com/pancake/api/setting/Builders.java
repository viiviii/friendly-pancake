package com.pancake.api.setting;

import com.pancake.api.setting.api.SettingApiController.EnableRequest;
import lombok.Builder;

import java.time.LocalDate;

public abstract class Builders {

    public static Builders.EnableRequestBuilder aEnabledSetting() {
        return enableSettingBuilder();
    }

    @Builder(builderMethodName = "enableSettingBuilder")
    private static EnableRequest create(LocalDate disabledAt) {
        return new EnableRequest(disabledAt);
    }
}