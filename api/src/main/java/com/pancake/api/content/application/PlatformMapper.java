package com.pancake.api.content.application;

import com.pancake.api.content.domain.Platform;
import com.pancake.api.content.domain.PlaybackUrl;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Stream;

@Component
public class PlatformMapper {

    public Platform mapFrom(ContentStreaming streaming) {
        return getPlatforms()
                .filter(platform -> canMap(streaming, platform))
                .findAny().orElseThrow(IllegalArgumentException::new);
    }

    private Stream<Platform> getPlatforms() {
        return Arrays.stream(Platform.values());
    }

    private boolean canMap(ContentStreaming streaming, Platform platform) {
        final var url = new PlaybackUrl(streaming.getUrl());

        return url.isSatisfiedBy(platform);
    }
}