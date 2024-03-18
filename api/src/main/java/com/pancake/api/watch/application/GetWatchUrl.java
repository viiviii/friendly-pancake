package com.pancake.api.watch.application;

import com.pancake.api.content.domain.Platform;
import com.pancake.api.watch.domain.WatchContent;
import com.pancake.api.watch.domain.WatchContentRepository;
import com.pancake.api.watch.domain.WatchOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetWatchUrl {

    private final WatchContentRepository watchContentRepository;
    
    public String query(Long id, Platform platform) {
        final var watchContent = contentBy(id);
        final var watchOption = optionFrom(watchContent, platform);

        return watchOption.getUrl();
    }

    private WatchContent contentBy(Long id) {
        return watchContentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    private WatchOption optionFrom(WatchContent content, Platform platform) {
        return content.getOptions().stream()
                .filter(e -> e.getPlatform().equals(platform))
                .findAny().orElseThrow(IllegalArgumentException::new);
    }
}