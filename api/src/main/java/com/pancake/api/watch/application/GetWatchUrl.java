package com.pancake.api.watch.application;

import com.pancake.api.content.domain.Platform;
import com.pancake.api.watch.domain.FindWatchOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetWatchUrl {

    private final FindWatchOption watchOption;

    public String query(Long id, Platform platform) {
        final var option = watchOption.findByContentIdAndPlatform(id, platform)
                .orElseThrow(IllegalArgumentException::new);

        return option.getUrl();
    }
}