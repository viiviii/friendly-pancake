package com.pancake.api.bookmark;

import com.pancake.api.content.application.ContentMetadata;
import com.pancake.api.search.FindContentMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetContentMetadata {

    private final FindContentMetadata findContentMetadata; // TODO: 위치

    public ContentMetadata queryBy(String contentId) {
        return findContentMetadata.findById(contentId);
    }
}
