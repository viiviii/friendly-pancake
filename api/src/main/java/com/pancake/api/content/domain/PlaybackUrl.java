package com.pancake.api.content.domain;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;


@Embeddable
@NoArgsConstructor(access = PRIVATE)
@EqualsAndHashCode
public final class PlaybackUrl {

    private Url url;

    public PlaybackUrl(String url) {
        this.url = new Url(url);
    }

    public boolean isSatisfiedBy(Platform platform) {
        return this.url.toString().startsWith(platform.baseUrl());
    }

    @Override
    public String toString() {
        return this.url.toString();
    }
}
