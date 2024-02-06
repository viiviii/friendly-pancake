package com.pancake.api.content.domain;

import lombok.RequiredArgsConstructor;

import java.net.URL;

@RequiredArgsConstructor
public class Watch {

    private final URL playbackUrl;

    public String getUrl() {
        return this.playbackUrl.toString();
    }
}
