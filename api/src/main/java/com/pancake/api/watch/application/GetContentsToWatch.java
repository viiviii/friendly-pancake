package com.pancake.api.watch.application;

import com.pancake.api.watch.domain.FindEnabledPlatforms;
import com.pancake.api.watch.domain.FindWatchContent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.Clock;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.MINUTES;

@Service
@RequiredArgsConstructor
public class GetContentsToWatch {

    private final FindWatchContent contents;

    private final FindEnabledPlatforms settings;

    private final Clock clock;

    public Catalog query() {
        final var enableCriteriaTime = now(clock).plus(5, MINUTES);
        final var enabledPlatforms = settings.findEnabledPlatformsAt(enableCriteriaTime);
        final var watchableContents = contents.findAll().stream()
                .filter(e -> e.canWatchOnAny(enabledPlatforms))
                .toList();

        return new Catalog(watchableContents);
    }

    @Bean
    private static Clock clock() {
        return Clock.systemUTC();
    }
}