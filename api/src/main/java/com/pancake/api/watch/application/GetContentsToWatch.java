package com.pancake.api.watch.application;

import com.pancake.api.watch.domain.WatchContent;
import com.pancake.api.watch.domain.WatchContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetContentsToWatch {

    private final WatchContentRepository watchContentRepository;

    public List<GetContentsToWatchResult> query() {
        return watchContentRepository.findAll().stream()
                .filter(WatchContent::canWatch)
                .map(GetContentsToWatchResult::new)
                .toList();
    }
}