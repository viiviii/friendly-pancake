package com.pancake.api.content.application;

import com.pancake.api.content.application.dto.ContentRequest;
import com.pancake.api.content.domain.Content;
import com.pancake.api.content.infra.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public List<Content> getUnwatchedContents() {
        return contentRepository.findByWatchedFalse();
    }

    public boolean watch(long id) {
        return true;
    }
}
