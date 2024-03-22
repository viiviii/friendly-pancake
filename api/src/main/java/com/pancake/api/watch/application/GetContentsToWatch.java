package com.pancake.api.watch.application;

import com.pancake.api.watch.domain.FindWatchContent;
import com.pancake.api.watch.domain.WatchContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetContentsToWatch {

    private final FindWatchContent watchContent;

    public List<WatchContent> query() {
        return watchContent.findAll().stream()
                .filter(WatchContent::canWatch)
                .toList();
    }
}