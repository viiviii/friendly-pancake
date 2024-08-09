package com.pancake.api.content.movie;

import com.pancake.api.content.ContentProvider;
import com.pancake.api.content.ContentType;
import com.pancake.api.content.application.ContentMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class MovieContentProvider implements ContentProvider {

    private final FindMovie findMovie;

    @Override
    public ContentType provideType() {
        return ContentType.movie;
    }

    @Override
    public ContentMetadata getBy(String contentId) {
        return findMovie.findById(contentId);
    }
}
