package com.pancake.api.content.domain;

import lombok.Getter;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


@Getter
public final class PlaybackUrl {
    private final URL url;

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
}
