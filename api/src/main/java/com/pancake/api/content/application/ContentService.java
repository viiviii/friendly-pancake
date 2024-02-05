package com.pancake.api.content.application;

import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Content getContent(long id) {
        return contentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public void watch(long id) {
        final var content = getContent(id);
        content.watch();
    }

    @Transactional
    public void addWatch(long id, AddWatchCommand command) {
        final var content = getContent(id);
        content.addUrl(command.getUrl());
    }
}
