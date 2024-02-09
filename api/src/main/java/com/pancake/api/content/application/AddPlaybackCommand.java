package com.pancake.api.content.application;

import com.pancake.api.content.domain.Playback;
import com.pancake.api.content.domain.PlaybackUrl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPlaybackCommand {

    private String url;

    public Playback toEntity() {
        final var playbackUrl = new PlaybackUrl(getUrl());
        return new Playback(playbackUrl);
    }
}
