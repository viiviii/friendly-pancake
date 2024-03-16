package com.pancake.api.watch.application;

import com.pancake.api.watch.domain.WatchContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetWatchUrl {

    private final WatchContentRepository watchContentRepository;

    public String query(Long id) {
        final var watch = watchContentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        return watch.getOptions().get(0).getUrl(); // TODO
    }
}