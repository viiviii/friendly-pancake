package com.pancake.api.content.application;

import com.pancake.api.content.application.dto.AddWatchRequest;
import com.pancake.api.content.application.dto.ContentRequest;
import com.pancake.api.content.domain.Content;
import com.pancake.api.content.infra.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    public Content save(ContentRequest request) {
        return contentRepository.save(request.toEntity());
    }

    public List<Content> getAllContents() {
        return contentRepository.findAll();
    }

    public Content getContent(long id) {
        return contentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new); // TODO
    }

    @Transactional
    public void watch(long id) {
        final var content = contentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new); // TODO

        content.watch();
    }

    @Transactional
    public void addWatch(long id, AddWatchRequest request) {
        final var content = contentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new); // TODO

        content.addUrl(request.getUrl());
    }
}
