package com.pancake.api.setting.domain;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

class DisableDateTime {

    private final ZonedDateTime disableAt;

    public DisableDateTime(ZonedDateTime dateTime) {
        this.disableAt = toUtc(dateTime);
    }

    private ZonedDateTime toUtc(ZonedDateTime dateTime) {
        return dateTime.withZoneSameInstant(ZoneOffset.UTC);
    }

    public boolean isAfter(ZonedDateTime other) {
        return disableAt.isAfter(other);
    }

    @Override
    public String toString() {
        return disableAt.toString();
    }
}
