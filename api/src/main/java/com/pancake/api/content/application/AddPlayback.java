package com.pancake.api.content.application;

import com.pancake.api.content.domain.Playback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddPlayback {

    private final ContentService contentService;

    private final PlatformMapper platformMapper;

    @Transactional
    public void command(long contentId, ContentStreaming streaming) {
        final var content = contentService.get(contentId);
        final var playback = toPlayback(streaming);

        content.add(playback);
    }

    private Playback toPlayback(ContentStreaming streaming) {
        final var platform = platformMapper.mapFrom(streaming);

        return new Playback(streaming.getUrl(), platform);
    }
}