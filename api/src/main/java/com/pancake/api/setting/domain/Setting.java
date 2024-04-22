package com.pancake.api.setting.domain;

import com.pancake.api.content.domain.Platform;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "settings")
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Setting {

    @Id
    @Enumerated(STRING)
    private Platform platform;

    private DisableDateTime disableFrom;

    public void disableFrom(DisableDateTime disableFrom) {
        this.disableFrom = disableFrom;
    }

    public boolean isEnabled() {
        return disableFrom == null || disableFrom.isAfter(Instant.now()); // TODO
    }
}
