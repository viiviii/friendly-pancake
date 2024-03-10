package com.pancake.api.content.application;

import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.ContentRepository;
import com.pancake.api.content.domain.Platform;
import com.pancake.api.content.domain.Playback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    public Content save(SaveContentCommand command) {
        return contentRepository.save(command.toEntity());
    }

    public List<Content> getAllContents() {
        return contentRepository.findAll().stream()
                .filter(Content::canWatch)
                .toList();
    }

    @Transactional
    public void watch(long id) {
        final var content = loadContentBy(id);
        content.watch();
    }

    @Transactional
    public void addPlayback(long id, AddPlaybackCommand command) {
        final var content = loadContentBy(id);
        final var playback = toPlayback(command);
        content.add(playback);
    }

    private Playback toPlayback(AddPlaybackCommand command) {
        final var platform = mapPlatformWith(command);
        return new Playback(command.getUrl(), platform);
    }

    private Platform mapPlatformWith(AddPlaybackCommand command) {
        return Arrays.stream(Platform.values())
                .filter(e -> command.getUrl().startsWith(e.baseUrl()))
                .findAny().orElseThrow(IllegalArgumentException::new);
    }

    private Content loadContentBy(long id) {
        return contentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}
