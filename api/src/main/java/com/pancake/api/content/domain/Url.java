package com.pancake.api.content.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static lombok.AccessLevel.PRIVATE;


@Embeddable
@NoArgsConstructor(access = PRIVATE)
@EqualsAndHashCode
final class Url {

    @Column(name = "url")
    private URL value;

    public Url(String url) {
        if (url == null) {
            throw new IllegalArgumentException();
        }
        this.value = parse(url);
    }

    private URL parse(String value) {
        try {
            return new URI(value).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
