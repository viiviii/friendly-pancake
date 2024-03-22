package com.pancake.api.watch.application;

import com.pancake.api.watch.domain.FindWatchContent;
import com.pancake.api.watch.domain.WatchContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetContentsToWatch {

    private final FindWatchContent watchContent;

    public Catalog query() {
        final var contents = watchContent.findAll().stream()
                .filter(WatchContent::canWatch)
                .toList();

        return new Catalog(contents);
    }
}