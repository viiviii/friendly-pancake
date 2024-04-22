package com.pancake.api.watch.application;

import com.pancake.api.watch.domain.FindEnabledPlatforms;
import com.pancake.api.watch.domain.FindWatchContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class GetContentsToWatch {

    private final FindWatchContent contents;

    private final FindEnabledPlatforms settings;

    public Catalog query() {
        final var enabledPlatforms = settings.findEnabledPlatformsAt(Instant.now());
        final var watchableContents = contents.findAll().stream()
                .filter(e -> e.canWatchOnAny(enabledPlatforms))
                .toList();

        return new Catalog(watchableContents);
    }
}