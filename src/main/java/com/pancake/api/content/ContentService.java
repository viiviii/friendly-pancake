package com.pancake.api.content;

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

    public List<Content> getAll() {
        return contentRepository.findAll();
    }
}
