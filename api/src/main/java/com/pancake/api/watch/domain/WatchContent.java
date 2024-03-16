package com.pancake.api.watch.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
@Getter
public class WatchContent {

    private final Long id;

    private final String title;

    private final String description;

    private final String imageUrl;

    private final boolean watched;

    private final List<WatchOption> options;

    public boolean isWatched() {
        return this.watched;
    }
    
    public boolean canWatch() {
        return !getOptions().isEmpty();
    }
}
