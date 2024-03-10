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
    public void with(long contentId, AddPlaybackCommand command) {
        final var content = contentService.getBy(contentId);
        final var watchOption = toWatchOption(command);

        content.add(watchOption);
    }

    private Playback toWatchOption(AddPlaybackCommand command) {
        final var platform = platformMapper.mapFrom(command);

        return new Playback(command.getUrl(), platform);
    }
}