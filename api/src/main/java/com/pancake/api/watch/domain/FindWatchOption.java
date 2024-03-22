package com.pancake.api.watch.domain;

import com.pancake.api.content.domain.Platform;

import java.util.Optional;

public interface FindWatchOption {
    
    Optional<WatchOption> findByContentIdAndPlatform(Long contentId, Platform platform);
}
