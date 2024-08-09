package com.pancake.api.bookmark;

import com.pancake.api.content.application.ContentMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieContentProvider implements ContentProvider {

    private final FindMovie findMovie;

    @Override
    public String provideType() {
        return "movie";
    }

    @Override
    public ContentMetadata getBy(String contentId) {
        return findMovie.findById(contentId);
    }
}
