package com.pancake.api.watch.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class WatchContent {

    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    private boolean watched;

    private List<WatchOption> options;

    public boolean isWatched() {
        return this.watched;
    }

    public boolean canWatch() {
        return !getOptions().isEmpty();
    }
}
