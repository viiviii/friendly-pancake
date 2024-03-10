package com.pancake.api.content.application;

import com.pancake.api.content.domain.Platform;
import com.pancake.api.content.domain.PlaybackUrl;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Stream;

@Component
public class PlatformMapper {

    public Platform mapFrom(AddPlaybackCommand command) {
        return getPlatforms()
                .filter(platform -> canMap(command, platform))
                .findAny().orElseThrow(IllegalArgumentException::new);
    }

    private Stream<Platform> getPlatforms() {
        return Arrays.stream(Platform.values());
    }

    private boolean canMap(AddPlaybackCommand command, Platform platform) {
        final var url = new PlaybackUrl(command.getUrl());

        return url.isSatisfiedBy(platform);
    }
}