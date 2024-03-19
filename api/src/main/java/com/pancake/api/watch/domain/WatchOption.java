package com.pancake.api.watch.domain;

import com.pancake.api.content.domain.Platform;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class WatchOption {

    private final Platform platform;
    private final String url;
}
