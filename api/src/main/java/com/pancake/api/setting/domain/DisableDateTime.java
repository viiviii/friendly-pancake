package com.pancake.api.setting.domain;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static lombok.AccessLevel.PRIVATE;

@Embeddable
@NoArgsConstructor(access = PRIVATE)
@EqualsAndHashCode
public class DisableDateTime {

    private Instant disableFrom;

    public DisableDateTime(Instant dateTime) {
        this.disableFrom = dateTime;
    }

    public boolean isAfter(Instant other) {
        return disableFrom.isAfter(other);
    }

    @Override
    public String toString() {
        return disableFrom.toString();
    }
}
