package com.pancake.api.content.application;

import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    public Content save(ContentSaveCommand command) {
        return contentRepository.save(command.toContent());
    }

    public Content get(long id) {
        return contentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public void watch(long id) {
        final var content = get(id);
        content.watch();
    }

    @Transactional
    public void changeImage(long id, String imageUrl) {
        final var content = get(id);
        content.change(imageUrl);
    }
}
