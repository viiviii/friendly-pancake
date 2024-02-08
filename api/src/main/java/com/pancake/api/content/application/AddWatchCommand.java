package com.pancake.api.content.application;

import com.pancake.api.content.domain.PlaybackUrl;
import com.pancake.api.content.domain.Watch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddWatchCommand {

    private String url;

    public Watch toEntity() {
        final var playbackUrl = new PlaybackUrl(getUrl());
        return new Watch(playbackUrl);
    }
}
