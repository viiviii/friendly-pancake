package com.pancake.api.content.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public final class Watch {

    private PlaybackUrl playbackUrl;
}
