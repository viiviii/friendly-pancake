package com.pancake.api.content.application;

import com.pancake.api.content.application.dto.ContentRequest;
import com.pancake.api.content.application.dto.ContentResponse;
import com.pancake.api.content.infra.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    public ContentResponse save(ContentRequest request) {
        final var saved = contentRepository.save(request.toEntity());

        return ContentResponse.fromEntity(saved);
    }

    public List<ContentResponse> getAllContents() {
        final var contents = contentRepository.findAll();

        return contents.stream()
                .map(ContentResponse::fromEntity)
                .toList();
    }

    public ContentResponse getContent(long id) {
        final var content = contentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        return ContentResponse.fromEntity(content);
    }

    @Transactional
    public boolean watch(long id) {
        final var content = contentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        return content.watch();
    }
}
