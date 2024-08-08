package com.pancake.api;

import com.pancake.api.bookmark.FindMovieMetadata;
import com.pancake.api.content.Builders;
import com.pancake.api.content.application.ContentMetadata;
import com.pancake.api.search.FindContentMetadata;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.HashMap;
import java.util.Map;

@TestComponent
public class MemoryMetadataRepository implements FindContentMetadata, FindMovieMetadata {

    private final Map<String, ContentMetadata> map = new HashMap<>();

    public void 존재한다(Builders.ContentMetadataBuilder builder) {
        var metadata = builder.build();
        map.put(metadata.getId(), metadata);
    }

    @Override
    public ContentMetadata findById(String id) {
        return map.get(id);
    }

    @Override
    public Page<ContentMetadata> findAllByTitle(String title) {
        return new PageImpl<>(map.values().stream()
                .filter(e -> e.getTitle().equals(title))
                .toList());
    }
}
