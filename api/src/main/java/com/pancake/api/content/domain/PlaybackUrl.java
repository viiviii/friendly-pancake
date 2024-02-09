package com.pancake.api.content.domain;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static lombok.AccessLevel.PRIVATE;


@Embeddable
@NoArgsConstructor(access = PRIVATE)
public final class PlaybackUrl {

    private URL url;

    public PlaybackUrl(String value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        this.url = parse(value);
    }

    private URL parse(String url) {
        try {
            return new URI(url).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String asString() {
        return this.url.toString();
    }
}
