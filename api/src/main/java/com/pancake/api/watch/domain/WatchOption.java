package com.pancake.api.watch.domain;

import com.pancake.api.content.domain.Platform;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class WatchOption {

    private Platform platform;

    private String url;
}
