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

    private Instant disableAt;

    public DisableDateTime(Instant dateTime) {
        this.disableAt = dateTime;
    }

    public boolean isAfter(Instant other) {
        return disableAt.isAfter(other);
    }

    @Override
    public String toString() {
        return disableAt.toString();
    }
}
