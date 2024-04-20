package com.pancake.api.setting.domain;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static lombok.AccessLevel.PRIVATE;

@Embeddable
@NoArgsConstructor(access = PRIVATE)
@EqualsAndHashCode
public class DisableDateTime {

    private ZonedDateTime disableAt;

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
